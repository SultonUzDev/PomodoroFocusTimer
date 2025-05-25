package com.sultonuzdev.pft.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.res.Configuration
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.sultonuzdev.pft.MainActivity
import com.sultonuzdev.pft.R
import com.sultonuzdev.pft.core.language.LanguageManager
import com.sultonuzdev.pft.core.util.Constants.MILLIS_IN_SECOND
import com.sultonuzdev.pft.core.util.TimerState
import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.core.util.calculateProgress
import com.sultonuzdev.pft.core.util.formatToMinutesAndSeconds
import com.sultonuzdev.pft.features.timer.domain.model.TimerSettings
import com.sultonuzdev.pft.features.timer.domain.usecase.GetTimerSettingsUseCase
import com.sultonuzdev.pft.service.TimerServiceConstants.ACTION_PAUSE
import com.sultonuzdev.pft.service.TimerServiceConstants.ACTION_RESUME
import com.sultonuzdev.pft.service.TimerServiceConstants.ACTION_SKIP
import com.sultonuzdev.pft.service.TimerServiceConstants.ACTION_START
import com.sultonuzdev.pft.service.TimerServiceConstants.ACTION_STOP
import com.sultonuzdev.pft.service.TimerServiceConstants.EXTRA_TIMER_DURATION
import com.sultonuzdev.pft.service.TimerServiceConstants.EXTRA_TIMER_TYPE
import com.sultonuzdev.pft.service.TimerServiceConstants.NOTIFICATION_CHANNEL_ID
import com.sultonuzdev.pft.service.TimerServiceConstants.NOTIFICATION_CHANNEL_NAME
import com.sultonuzdev.pft.service.TimerServiceConstants.NOTIFICATION_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.util.Locale
import javax.inject.Inject

private const val TAG = "TimerService"

/**
 * Foreground service for managing the Pomodoro timer in the background
 * This is the single source of truth for timer state
 * Now properly integrates with timer settings from repository
 */
@AndroidEntryPoint
class TimerService : Service() {

    @Inject
    lateinit var getTimerSettingsUseCase: GetTimerSettingsUseCase

    private val serviceScope = CoroutineScope(Dispatchers.Default)
    private var timerJob: Job? = null
    private var settingsJob: Job? = null

    // Start time for session tracking
    private var startTime: LocalDateTime? = null

    // Current settings cache
    private var currentSettings: TimerSettings = TimerSettings()

    // Timer state flows - These are the single source of truth
    private val _timerState = MutableStateFlow(TimerState.IDLE)
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    private val _currentTimerType = MutableStateFlow(TimerType.POMODORO)
    val currentTimerType: StateFlow<TimerType> = _currentTimerType.asStateFlow()

    private val _remainingTimeMillis = MutableStateFlow(25 * 60 * 1000L) // Will be updated from settings
    val remainingTimeMillis: StateFlow<Long> = _remainingTimeMillis.asStateFlow()

    private val _totalTimeMillis = MutableStateFlow(25 * 60 * 1000L) // Will be updated from settings
    val totalTimeMillis: StateFlow<Long> = _totalTimeMillis.asStateFlow()

    private val _progressFraction = MutableStateFlow(1.0f)
    val progressFraction: StateFlow<Float> = _progressFraction.asStateFlow()

    private val _formattedTime = MutableStateFlow("25:00")
    val formattedTime: StateFlow<String> = _formattedTime.asStateFlow()

    private val _completedPomodoros = MutableStateFlow(0)
    val completedPomodoros: StateFlow<Int> = _completedPomodoros.asStateFlow()

    private val _completedSessions = MutableStateFlow(0)
    val completedSessions: StateFlow<Int> = _completedSessions.asStateFlow()

    private val binder = TimerBinder()

    inner class TimerBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service onCreate")
        createNotificationChannel()
        observeTimerSettings()
    }

    @Inject
    lateinit var languageManager: LanguageManager

    // Add this method to get localized strings:
    private fun getLocalizedString(stringRes: Int, vararg formatArgs: Any): String {
        return try {
            // Get current language
            val currentLanguage = runBlocking { languageManager.getCurrentLanguage() }
            val locale = Locale(currentLanguage.code)
            val config = Configuration(resources.configuration)
            config.setLocale(locale)
            val localizedContext = createConfigurationContext(config)

            if (formatArgs.isNotEmpty()) {
                localizedContext.getString(stringRes, *formatArgs)
            } else {
                localizedContext.getString(stringRes)
            }
        } catch (e: Exception) {
            // Fallback to default
            if (formatArgs.isNotEmpty()) {
                getString(stringRes, *formatArgs)
            } else {
                getString(stringRes)
            }
        }
    }

    private fun observeTimerSettings() {
        settingsJob = serviceScope.launch {
            try {
                getTimerSettingsUseCase().collectLatest { settings ->
                    Log.d(TAG, "Settings updated: $settings")
                    currentSettings = settings

                    // Update timer durations if timer is idle
                    if (_timerState.value == TimerState.IDLE) {
                        updateTimerDurationsFromSettings()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error observing settings", e)
            }
        }
    }

    private fun updateTimerDurationsFromSettings() {
        val duration = getDurationForTimerType(_currentTimerType.value)
        _totalTimeMillis.value = duration
        _remainingTimeMillis.value = duration
        _formattedTime.value = duration.formatToMinutesAndSeconds()
        _progressFraction.value = 1.0f
        Log.d(TAG, "Updated timer duration to ${duration}ms for ${_currentTimerType.value}")
    }

    private fun getDurationForTimerType(timerType: TimerType): Long {
        return when (timerType) {
            TimerType.POMODORO -> currentSettings.pomodoroMinutes * 60 * 1000L
            TimerType.SHORT_BREAK -> currentSettings.shortBreakMinutes * 60 * 1000L
            TimerType.LONG_BREAK -> currentSettings.longBreakMinutes * 60 * 1000L
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service onStartCommand: ${intent?.action}")

        intent?.let {
            when (intent.action) {
                ACTION_START -> {
                    val timerType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent.getSerializableExtra(EXTRA_TIMER_TYPE, TimerType::class.java) ?: TimerType.POMODORO
                    } else {
                        @Suppress("DEPRECATION")
                        intent.getSerializableExtra(EXTRA_TIMER_TYPE) as? TimerType ?: TimerType.POMODORO
                    }
                    val duration = intent.getLongExtra(EXTRA_TIMER_DURATION, getDurationForTimerType(timerType))
                    startTimer(timerType, duration)
                }
                ACTION_PAUSE -> pauseTimer()
                ACTION_RESUME -> resumeTimer()
                ACTION_STOP -> stopTimer()
                ACTION_SKIP -> skipTimer()
            }
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.d(TAG, "Service onBind")
        return binder
    }

    override fun onDestroy() {
        Log.d(TAG, "Service onDestroy")
        timerJob?.cancel()
        settingsJob?.cancel()
        super.onDestroy()
    }

    fun startTimer(timerType: TimerType, durationMillis: Long) {
        Log.d(TAG, "startTimer: type=$timerType, duration=$durationMillis")

        // Cancel any existing timer
        timerJob?.cancel()

        // Use duration from settings if provided duration is default
        val actualDuration = if (durationMillis == getDurationForTimerType(timerType)) {
            getDurationForTimerType(timerType)
        } else {
            durationMillis
        }

        // Update all state flows immediately
        _currentTimerType.value = timerType
        _totalTimeMillis.value = actualDuration
        _remainingTimeMillis.value = actualDuration
        _formattedTime.value = actualDuration.formatToMinutesAndSeconds()
        _progressFraction.value = 1.0f
        _timerState.value = TimerState.RUNNING

        startTime = LocalDateTime.now()

        // Start foreground service
        try {
            startForegroundService()
        } catch (e: Exception) {
            Log.e(TAG, "Error starting foreground service", e)
        }

        // Start the timer coroutine
        timerJob = serviceScope.launch {
            try {
                while (_remainingTimeMillis.value > 0 && _timerState.value == TimerState.RUNNING) {
                    delay(MILLIS_IN_SECOND)

                    // Only update if still running (not paused)
                    if (_timerState.value == TimerState.RUNNING) {
                        val newRemainingTime = (_remainingTimeMillis.value - MILLIS_IN_SECOND).coerceAtLeast(0)
                        val newProgress = calculateProgress(
                            _totalTimeMillis.value - newRemainingTime,
                            _totalTimeMillis.value
                        )

                        // Update state flows
                        _remainingTimeMillis.value = newRemainingTime
                        _formattedTime.value = newRemainingTime.formatToMinutesAndSeconds()
                        _progressFraction.value = newProgress

                        updateNotification()

                        // Check if timer completed
                        if (newRemainingTime <= 0) {
                            timerCompleted()
                            break
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error in timer coroutine", e)
            }
        }
    }

    fun pauseTimer() {
        Log.d(TAG, "pauseTimer called")
        if (_timerState.value == TimerState.RUNNING) {
            _timerState.value = TimerState.PAUSED
            updateNotification()
        }
    }

    fun resumeTimer() {
        Log.d(TAG, "resumeTimer called")
        if (_timerState.value == TimerState.PAUSED) {
            _timerState.value = TimerState.RUNNING
            updateNotification()

            // Resume the timer coroutine with remaining time
            startTimer(_currentTimerType.value, _remainingTimeMillis.value)
        }
    }

    fun stopTimer() {
        Log.d(TAG, "stopTimer called")

        // Cancel timer coroutine
        timerJob?.cancel()
        timerJob = null

        // Reset state to initial values based on current settings
        val defaultDuration = getDurationForTimerType(_currentTimerType.value)

        _timerState.value = TimerState.IDLE
        _totalTimeMillis.value = defaultDuration
        _remainingTimeMillis.value = defaultDuration
        _formattedTime.value = defaultDuration.formatToMinutesAndSeconds()
        _progressFraction.value = 1.0f

        // Stop foreground service
        try {
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping foreground service", e)
        }

        startTime = null
    }

    fun skipTimer() {
        Log.d(TAG, "skipTimer called")

        // Cancel timer coroutine
        timerJob?.cancel()
        timerJob = null

        // Mark as completed
        _timerState.value = TimerState.COMPLETED
        _remainingTimeMillis.value = 0
        _formattedTime.value = "00:00"
        _progressFraction.value = 1.0f

        // Update completed count if it was a Pomodoro
        if (_currentTimerType.value == TimerType.POMODORO) {
            _completedPomodoros.value += 1
        }

        updateNotification(completed = true)
        startTime = null

        // Auto-transition after a short delay
        serviceScope.launch {
            delay(2000) // 2 second delay
            val nextType = getNextTimerType()
            changeTimerType(nextType)
        }
    }

    private fun timerCompleted() {
        Log.d(TAG, "timerCompleted")

        timerJob?.cancel()
        timerJob = null

        val currentType = _currentTimerType.value

        // Update state
        _timerState.value = TimerState.COMPLETED
        _remainingTimeMillis.value = 0
        _formattedTime.value = "00:00"
        _progressFraction.value = 1.0f

        // Update completed count if it was a Pomodoro
        if (currentType == TimerType.POMODORO) {
            _completedPomodoros.value += 1
            _completedSessions.value += 1
        }

        updateNotification(completed = true)
        startTime = null

        // Auto-transition to next timer type after delay
        serviceScope.launch {
            delay(2000) // 2 second delay to show completion
            val nextType = getNextTimerType()
            changeTimerType(nextType)
        }
    }

    private fun changeTimerType(type: TimerType) {
        Log.d(TAG, "changeTimerType to $type")

        // Cancel any running timer
        timerJob?.cancel()
        timerJob = null

        // Set new timer type and duration based on current settings
        val newDuration = getDurationForTimerType(type)

        _currentTimerType.value = type
        _timerState.value = TimerState.IDLE
        _totalTimeMillis.value = newDuration
        _remainingTimeMillis.value = newDuration
        _formattedTime.value = newDuration.formatToMinutesAndSeconds()
        _progressFraction.value = 1.0f

        startTime = null
    }

    private fun getNextTimerType(): TimerType {
        val currentType = _currentTimerType.value
        val completedPomodoros = _completedPomodoros.value
        val pomodorosBeforeLongBreak = currentSettings.pomodorosBeforeLongBreak

        return when (currentType) {
            TimerType.POMODORO -> {
                // After X pomodoros, take a long break, otherwise take a short break
                if (completedPomodoros % pomodorosBeforeLongBreak == 0) {
                    TimerType.LONG_BREAK
                } else {
                    TimerType.SHORT_BREAK
                }
            }
            TimerType.SHORT_BREAK, TimerType.LONG_BREAK -> TimerType.POMODORO
        }
    }

    // Expose method to manually change timer type (called from ViewModel)
    fun changeTimerTypeManually(type: TimerType) {
        if (_timerState.value == TimerState.IDLE) {
            changeTimerType(type)
        } else {
            // Stop the current timer first, then change type
            Log.d(TAG, "Stopping current timer to change type")
            stopTimer()
            // Small delay to ensure stop completes, then change type
            serviceScope.launch {
                kotlinx.coroutines.delay(100)
                changeTimerType(type)
            }
        }
    }

    // Expose method to get current settings
    fun getCurrentSettings(): TimerSettings = currentSettings

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                val channel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "Pomodoro Timer notifications"
                    setShowBadge(false)
                }

                val notificationManager = getSystemService(NotificationManager::class.java)
                notificationManager.createNotificationChannel(channel)
                Log.d(TAG, "Notification channel created")
            } catch (e: Exception) {
                Log.e(TAG, "Error creating notification channel", e)
            }
        }
    }

    private fun startForegroundService() {
        try {
            val notification = createNotification()
            startForeground(NOTIFICATION_ID, notification)
            Log.d(TAG, "Started foreground service")
        } catch (e: Exception) {
            Log.e(TAG, "Error starting foreground service", e)
        }
    }

    private fun updateNotification(completed: Boolean = false) {
        try {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val notification = createNotification(completed)
            notificationManager.notify(NOTIFICATION_ID, notification)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating notification", e)
        }
    }

    private fun createNotification(completed: Boolean = false): Notification {
        val title = when (_currentTimerType.value) {
            TimerType.POMODORO -> "Focus Time"
            TimerType.SHORT_BREAK -> "Short Break"
            TimerType.LONG_BREAK -> "Long Break"
        }

        val contentText = if (completed) {
            when (_currentTimerType.value) {
                TimerType.POMODORO -> "Focus session completed!"
                TimerType.SHORT_BREAK -> "Break time over!"
                TimerType.LONG_BREAK -> "Long break completed!"
            }
        } else {
            when (_timerState.value) {
                TimerState.RUNNING -> "${_formattedTime.value} remaining"
                TimerState.PAUSED -> "Paused - ${_formattedTime.value} remaining"
                else -> "Timer ready"
            }
        }

        // Create intent to return to app
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_logo)
            .setContentIntent(pendingIntent)
            .setOngoing(!completed)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)

        // Add action buttons based on current state
        when (_timerState.value) {
            TimerState.RUNNING -> {
                builder.addAction(createAction(ACTION_PAUSE, "Pause", R.drawable.ic_pause))
                builder.addAction(createAction(ACTION_STOP, "Stop", R.drawable.ic_stop))
                builder.addAction(createAction(ACTION_SKIP, "Skip", R.drawable.ic_skip))
            }
            TimerState.PAUSED -> {
                builder.addAction(createAction(ACTION_RESUME, "Resume", R.drawable.ic_play))
                builder.addAction(createAction(ACTION_STOP, "Stop", R.drawable.ic_stop))
            }
            else -> { /* No actions for idle/completed */ }
        }

        return builder.build()
    }

    private fun createAction(action: String, title: String, iconRes: Int): NotificationCompat.Action {
        val intent = Intent(this, TimerService::class.java).apply {
            this.action = action
        }
        val pendingIntent = PendingIntent.getService(
            this, action.hashCode(), intent, PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Action(iconRes, title, pendingIntent)
    }
}