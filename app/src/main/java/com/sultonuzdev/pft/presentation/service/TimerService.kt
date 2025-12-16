package com.sultonuzdev.pft.presentation.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat
import com.sultonuzdev.pft.MainActivity
import com.sultonuzdev.pft.R
import com.sultonuzdev.pft.core.util.Constants.MILLIS_IN_SECOND
import com.sultonuzdev.pft.core.util.Constants.NOTIFICATION_UPDATE_INTERVAL_MILLIS
import com.sultonuzdev.pft.core.util.Constants.PAUSE_LOOP_DELAY_MILLIS
import com.sultonuzdev.pft.core.util.Constants.TIMER_COMPLETION_DELAY_MILLIS
import com.sultonuzdev.pft.core.util.TimerState
import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.core.util.calculateProgress
import com.sultonuzdev.pft.core.util.formatToMinutesAndSeconds
import com.sultonuzdev.pft.domain.model.PomodoroTimerSettings
import com.sultonuzdev.pft.domain.repository.SettingsRepository
import com.sultonuzdev.pft.domain.usecase.pomodoro.AddPomodoro
import com.sultonuzdev.pft.presentation.service.TimerServiceConstants.ACTION_FINISH
import com.sultonuzdev.pft.presentation.service.TimerServiceConstants.ACTION_PAUSE
import com.sultonuzdev.pft.presentation.service.TimerServiceConstants.ACTION_RESUME
import com.sultonuzdev.pft.presentation.service.TimerServiceConstants.ACTION_SKIP
import com.sultonuzdev.pft.presentation.service.TimerServiceConstants.ACTION_START
import com.sultonuzdev.pft.presentation.service.TimerServiceConstants.EXTRA_TIMER_DURATION
import com.sultonuzdev.pft.presentation.service.TimerServiceConstants.EXTRA_TIMER_TYPE
import com.sultonuzdev.pft.presentation.service.TimerServiceConstants.NOTIFICATION_CHANNEL_ID
import com.sultonuzdev.pft.presentation.service.TimerServiceConstants.NOTIFICATION_CHANNEL_NAME
import com.sultonuzdev.pft.presentation.service.TimerServiceConstants.NOTIFICATION_ID
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
import java.time.LocalDate
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
    lateinit var settingsRepository: SettingsRepository

    @Inject
    lateinit var addPomodoro: AddPomodoro

    private val serviceScope = CoroutineScope(Dispatchers.Default)
    private var timerJob: Job? = null
    private var settingsJob: Job? = null

    // Start time for session tracking
    private var startTime: LocalDate? = null

    // Timer tracking using SystemClock for reliability during device lock
    private var timerStartElapsedTime: Long = 0L  // When timer started (SystemClock)
    private var pausedElapsedTime: Long = 0L      // Total time spent paused

    // Notification update throttling
    private var lastNotificationUpdateTime: Long = 0L

    // Current settings cache
    private var currentSettings: PomodoroTimerSettings = PomodoroTimerSettings()

    // Timer state flows - These are the single source of truth
    private val _timerState = MutableStateFlow(TimerState.IDLE)
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    private val _currentTimerType = MutableStateFlow(TimerType.POMODORO)
    val currentTimerType: StateFlow<TimerType> = _currentTimerType.asStateFlow()

    private val _remainingTimeMillis =
        MutableStateFlow(25 * 60 * 1000L) // Will be updated from settings
    val remainingTimeMillis: StateFlow<Long> = _remainingTimeMillis.asStateFlow()

    private val _totalTimeMillis =
        MutableStateFlow(25 * 60 * 1000L) // Will be updated from settings
    val totalTimeMillis: StateFlow<Long> = _totalTimeMillis.asStateFlow()

    private val _progressFraction = MutableStateFlow(1.0f)
    val progressFraction: StateFlow<Float> = _progressFraction.asStateFlow()

    private val _formattedTime = MutableStateFlow("25:00")
    val formattedTime: StateFlow<String> = _formattedTime.asStateFlow()


    private val _completedSessions = MutableStateFlow(0)
    val completedSessions: StateFlow<Int> = _completedSessions.asStateFlow()


    private val _completedPomodoros = MutableStateFlow(0) // Total lifetime count
    val completedPomodoros: StateFlow<Int> = _completedPomodoros.asStateFlow()

    private val _currentSessionPomodoros = MutableStateFlow(0) // Current session count
    val currentSessionPomodoros: StateFlow<Int> = _currentSessionPomodoros.asStateFlow()

    private val _totalSessions = MutableStateFlow(0) // Total completed sessions
    val totalSessions: StateFlow<Int> = _totalSessions.asStateFlow()


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


    private fun observeTimerSettings() {
        settingsJob = serviceScope.launch {
            try {
                settingsRepository.getDefaultSettings().collectLatest { settings ->
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


    private fun getNextTimerType(): TimerType {
        val currentType = _currentTimerType.value
        val sessionPomodoros = _currentSessionPomodoros.value // Use session count, not total
        val pomodorosBeforeLongBreak = currentSettings.pomodoroCycleLength

        Log.d(TAG, "getNextTimerType: currentType=$currentType, sessionPomodoros=$sessionPomodoros, pomodorosBeforeLongBreak=$pomodorosBeforeLongBreak")

        return when (currentType) {
            TimerType.POMODORO -> {
                if (sessionPomodoros >= pomodorosBeforeLongBreak) {
                    Log.d(TAG, "Session complete - taking long break")
                    TimerType.LONG_BREAK
                } else {
                    Log.d(TAG, "Taking short break")
                    TimerType.SHORT_BREAK
                }
            }

            TimerType.SHORT_BREAK -> {
                Log.d(TAG, "Short break over - back to Pomodoro")
                TimerType.POMODORO
            }

            TimerType.LONG_BREAK -> {
                Log.d(TAG, "Long break over - starting new session")
                // Reset session when long break is over
                _currentSessionPomodoros.value = 0
                _totalSessions.value += 1
                TimerType.POMODORO
            }
        }
    }

    // Update your timerCompleted method:
    private fun timerCompleted() {
        Log.d(TAG, "timerCompleted")

        timerJob?.cancel()
        timerJob = null

        val currentType = _currentTimerType.value
        val sessionStartTime = startTime

        // IMPORTANT: Capture values BEFORE resetting state to avoid race condition
        val capturedTotal = _totalTimeMillis.value
        val capturedRemaining = _remainingTimeMillis.value

        // Save ALL timer types to database (POMODORO, SHORT_BREAK, LONG_BREAK)
        if (sessionStartTime != null) {
            serviceScope.launch {
                try {
                    // Calculate focused duration in seconds (not milliseconds)
                    val focusedMillis = capturedTotal - capturedRemaining
                    val focusedSeconds = (focusedMillis / 1000).coerceAtLeast(0)

                    // Get planned duration based on timer type
                    val plannedSeconds = when (currentType) {
                        TimerType.POMODORO -> (currentSettings.pomodoroMinutes * 60).toLong()
                        TimerType.SHORT_BREAK -> (currentSettings.shortBreakMinutes * 60).toLong()
                        TimerType.LONG_BREAK -> (currentSettings.longBreakMinutes * 60).toLong()
                    }

                    addPomodoro(
                        type = currentType,
                        plannedDurationSeconds = plannedSeconds,
                        completed = true,
                        startedTime = sessionStartTime,
                        focusedDurationSeconds = focusedSeconds,
                    )
                    Log.d(
                        TAG,
                        "Completed $currentType session saved to database (${focusedSeconds}s)"
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to save completed session", e)
                }
            }
        }

        // Update state
        _timerState.value = TimerState.COMPLETED
        _remainingTimeMillis.value = 0
        _formattedTime.value = "00:00"
        _progressFraction.value = 1.0f

        // Update counters if it was a Pomodoro
        if (currentType == TimerType.POMODORO) {
            _completedPomodoros.value += 1 // Lifetime total
            _currentSessionPomodoros.value += 1 // Current session
            Log.d(
                TAG,
                "Pomodoro completed - Total: ${_completedPomodoros.value}, Session: ${_currentSessionPomodoros.value}"
            )
        }

        updateNotification(completed = true)
        startTime = null

        // Auto-transition to next timer type after delay
        serviceScope.launch {
            delay(TIMER_COMPLETION_DELAY_MILLIS) // Delay to show completion
            val nextType = getNextTimerType()
            changeTimerType(nextType)
        }
    }

    // Update your finishTimer method similarly:
    fun finishTimer() {
        Log.d(TAG, "finishTimer called - user stopped timer early")

        timerJob?.cancel()
        timerJob = null

        val currentType = _currentTimerType.value
        val sessionStartTime = startTime

        // IMPORTANT: Capture values BEFORE resetting state to avoid race condition
        val capturedTotal = _totalTimeMillis.value
        val capturedRemaining = _remainingTimeMillis.value

        // Save ALL timer types to database (marked as NOT completed since user stopped early)
        if (sessionStartTime != null) {
            serviceScope.launch {
                try {
                    // Calculate focused duration in seconds (not milliseconds)
                    val focusedMillis = capturedTotal - capturedRemaining
                    val focusedSeconds = (focusedMillis / 1000).coerceAtLeast(0)

                    // Get planned duration based on timer type
                    val plannedSeconds = when (currentType) {
                        TimerType.POMODORO -> (currentSettings.pomodoroMinutes * 60).toLong()
                        TimerType.SHORT_BREAK -> (currentSettings.shortBreakMinutes * 60).toLong()
                        TimerType.LONG_BREAK -> (currentSettings.longBreakMinutes * 60).toLong()
                    }

                    addPomodoro(
                        type = currentType,
                        plannedDurationSeconds = plannedSeconds,
                        completed = false, // User stopped early, NOT completed
                        startedTime = sessionStartTime,
                        focusedDurationSeconds = focusedSeconds,
                    )
                    Log.d(
                        TAG,
                        "Stopped $currentType session saved to database as incomplete (${focusedSeconds}s/${plannedSeconds}s)"
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to save stopped session", e)
                }
            }
        }

        // Update state
        _timerState.value = TimerState.COMPLETED
        _remainingTimeMillis.value = 0
        _formattedTime.value = "00:00"
        _progressFraction.value = 1.0f

        // DO NOT increment counters - user didn't complete the full session

        updateNotification(completed = true)
        startTime = null

        // Auto-transition after a short delay
        serviceScope.launch {
            delay(TIMER_COMPLETION_DELAY_MILLIS)
            val nextType = getNextTimerType()
            changeTimerType(nextType)
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
                        intent.getSerializableExtra(EXTRA_TIMER_TYPE, TimerType::class.java)
                            ?: TimerType.POMODORO
                    } else {
                        @Suppress("DEPRECATION")
                        intent.getSerializableExtra(EXTRA_TIMER_TYPE) as? TimerType
                            ?: TimerType.POMODORO
                    }
                    val duration = intent.getLongExtra(
                        EXTRA_TIMER_DURATION,
                        getDurationForTimerType(timerType)
                    )
                    startTimer(timerType, duration)
                }

                ACTION_PAUSE -> pauseTimer()
                ACTION_RESUME -> resumeTimer()
                ACTION_FINISH -> finishTimer()
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


    // Here's the complete fixed TimerService with proper pause/resume functionality:

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

        startTime = LocalDate.now()

        // Start foreground service
        try {
            startForegroundService()
        } catch (e: Exception) {
            Log.e(TAG, "Error starting foreground service", e)
        }

        // Start the timer coroutine
        startTimerCoroutine()
    }

    private fun startTimerCoroutine() {
        // Record when timer starts using SystemClock (survives screen lock)
        timerStartElapsedTime = SystemClock.elapsedRealtime()
        pausedElapsedTime = 0L
        lastNotificationUpdateTime = 0L  // Force immediate notification update

        timerJob = serviceScope.launch {
            try {
                while (_remainingTimeMillis.value > 0) {
                    // Check if we should continue (not paused or stopped)
                    if (_timerState.value != TimerState.RUNNING) {
                        Log.d(TAG, "Timer coroutine paused/stopped, waiting...")
                        delay(PAUSE_LOOP_DELAY_MILLIS) // Small delay to prevent tight loop
                        continue
                    }

                    delay(MILLIS_IN_SECOND)

                    // Only update if still running
                    if (_timerState.value == TimerState.RUNNING) {
                        // Calculate elapsed time using SystemClock (reliable during screen lock)
                        val currentElapsedTime = SystemClock.elapsedRealtime()
                        val actualElapsedTime =
                            currentElapsedTime - timerStartElapsedTime - pausedElapsedTime
                        val newRemainingTime =
                            (_totalTimeMillis.value - actualElapsedTime).coerceAtLeast(0)

                        val newProgress = calculateProgress(
                            _totalTimeMillis.value - newRemainingTime,
                            _totalTimeMillis.value
                        )

                        // Update state flows (UI updates every second for smooth animation)
                        _remainingTimeMillis.value = newRemainingTime
                        _formattedTime.value = newRemainingTime.formatToMinutesAndSeconds()
                        _progressFraction.value = newProgress

                        // Update notification only every 5 seconds to save battery
                        val timeSinceLastUpdate = currentElapsedTime - lastNotificationUpdateTime
                        if (timeSinceLastUpdate >= NOTIFICATION_UPDATE_INTERVAL_MILLIS) {
                            updateNotification()
                            lastNotificationUpdateTime = currentElapsedTime
                        }

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
        Log.d(TAG, "pauseTimer called, current state: ${_timerState.value}")
        if (_timerState.value == TimerState.RUNNING) {
            // Record when we paused (to calculate pause duration later)
            val pauseStartTime = SystemClock.elapsedRealtime()

            // Change state - coroutine will detect this and pause
            _timerState.value = TimerState.PAUSED
            updateNotification()  // Update immediately for state change

            // Store pause start time for resume calculation
            timerStartElapsedTime = pauseStartTime - (timerStartElapsedTime + pausedElapsedTime)

            Log.d(TAG, "Timer paused at ${_formattedTime.value}")
        } else {
            Log.d(TAG, "Timer not running, cannot pause")
        }
    }

    fun resumeTimer() {
        Log.d(TAG, "resumeTimer called, current state: ${_timerState.value}")
        if (_timerState.value == TimerState.PAUSED) {
            // Calculate how long we were paused and add to total pause time
            val resumeTime = SystemClock.elapsedRealtime()
            pausedElapsedTime += (resumeTime - timerStartElapsedTime)

            // Reset start time to current moment
            timerStartElapsedTime = SystemClock.elapsedRealtime()

            // Adjust for remaining time
            val currentRemaining = _remainingTimeMillis.value
            _totalTimeMillis.value = currentRemaining
            timerStartElapsedTime = SystemClock.elapsedRealtime()
            pausedElapsedTime = 0L

            // Change state - coroutine will detect this and resume
            _timerState.value = TimerState.RUNNING
            updateNotification()  // Update immediately for state change
            lastNotificationUpdateTime = SystemClock.elapsedRealtime()  // Reset throttle timer
            Log.d(TAG, "Timer resumed at ${_formattedTime.value}")
        } else {
            Log.d(TAG, "Timer not paused, cannot resume")
        }
    }


    fun skipTimer() {
        Log.d(TAG, "skipTimer called - user skipped timer, no save")

        // Cancel timer coroutine
        timerJob?.cancel()
        timerJob = null

        val currentType = _currentTimerType.value

        // DO NOT save to database - user wants to skip without recording

        // Update state to show completion
        _timerState.value = TimerState.COMPLETED
        _remainingTimeMillis.value = 0
        _formattedTime.value = "00:00"
        _progressFraction.value = 1.0f

        // DO NOT increment counters - user skipped this timer

        updateNotification(completed = true)
        startTime = null

        // Auto-transition to next timer type after a short delay
        serviceScope.launch {
            delay(TIMER_COMPLETION_DELAY_MILLIS)
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

    private fun createNotificationChannel() {
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
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)

        // Add action buttons based on current state
        when (_timerState.value) {
            TimerState.RUNNING -> {
                builder.addAction(createAction(ACTION_PAUSE, "Pause", R.drawable.ic_pause))
                builder.addAction(createAction(ACTION_SKIP, "Skip", R.drawable.ic_skip))
            }

            TimerState.PAUSED -> {
                builder.addAction(createAction(ACTION_RESUME, "Resume", R.drawable.ic_play))
                builder.addAction(createAction(ACTION_FINISH, "Finish", R.drawable.ic_stop))
            }

            else -> { /* No actions for idle/completed */ }

        }

        return builder.build()
    }

    private fun createAction(
        action: String,
        title: String,
        iconRes: Int
    ): NotificationCompat.Action {
        val intent = Intent(this, TimerService::class.java).apply {
            this.action = action
        }
        val pendingIntent = PendingIntent.getService(
            this, action.hashCode(), intent, PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Action(iconRes, title, pendingIntent)
    }
}