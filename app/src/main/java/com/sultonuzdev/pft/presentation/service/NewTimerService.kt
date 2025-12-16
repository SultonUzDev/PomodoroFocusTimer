package com.sultonuzdev.pft.presentation.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.sultonuzdev.pft.MainActivity
import com.sultonuzdev.pft.R
import com.sultonuzdev.pft.core.util.TimerState
import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.domain.repository.TimerRepository
import com.sultonuzdev.pft.presentation.service.TimerServiceConstants.ACTION_FINISH
import com.sultonuzdev.pft.presentation.service.TimerServiceConstants.ACTION_PAUSE
import com.sultonuzdev.pft.presentation.service.TimerServiceConstants.ACTION_RESUME
import com.sultonuzdev.pft.presentation.service.TimerServiceConstants.ACTION_SKIP
import com.sultonuzdev.pft.presentation.service.TimerServiceConstants.ACTION_START
import com.sultonuzdev.pft.presentation.service.TimerServiceConstants.NOTIFICATION_CHANNEL_ID
import com.sultonuzdev.pft.presentation.service.TimerServiceConstants.NOTIFICATION_CHANNEL_NAME
import com.sultonuzdev.pft.presentation.service.TimerServiceConstants.NOTIFICATION_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "NewTimerService"

/**
 * Modern foreground service that delegates timer logic to TimerRepository
 * This service only handles Android-specific concerns:
 * - Foreground service lifecycle
 * - Notification management
 * - Intent handling
 *
 * All timer business logic is in TimerRepository
 */
@AndroidEntryPoint
class NewTimerService : Service() {

    @Inject
    lateinit var timerRepository: TimerRepository

    private val serviceScope = CoroutineScope(Dispatchers.Main)
    private var stateObserverJob: Job? = null

    private var lastNotificationUpdateTime: Long = 0L
    private val NOTIFICATION_UPDATE_INTERVAL = 5000L // 5 seconds

    private val binder = NewTimerBinder()

    inner class NewTimerBinder : Binder() {
        fun getService(): NewTimerService = this@NewTimerService
        fun getRepository(): TimerRepository = timerRepository
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service onCreate")
        createNotificationChannel()
        observeTimerState()
    }

    private fun observeTimerState() {
        stateObserverJob = serviceScope.launch {
            timerRepository.timerState.collectLatest { state ->
                Log.d(TAG, "Timer state changed: ${state.timerState}, type: ${state.currentType}")

                // Update notification when state changes
                when (state.timerState) {
                    TimerState.RUNNING -> {
                        // Update notification every 5 seconds when running
                        val currentTime = System.currentTimeMillis()
                        if (currentTime - lastNotificationUpdateTime >= NOTIFICATION_UPDATE_INTERVAL) {
                            updateNotification(state)
                            lastNotificationUpdateTime = currentTime
                        }
                    }
                    TimerState.PAUSED, TimerState.COMPLETED -> {
                        updateNotification(state)
                    }
                    TimerState.IDLE -> {
                        // Timer is idle, potentially stop foreground
                        // Keep service alive but can remove notification
                    }
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service onStartCommand: ${intent?.action}")

        // IMPORTANT: Service only handles notifications, NOT timer logic
        // Timer logic is controlled by ViewModel -> Repository
        // Service just observes repository state and shows notifications

        // Start as foreground service immediately
        if (intent?.action == ACTION_START) {
            try {
                startForegroundService()
                Log.d(TAG, "Service started as foreground")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to start foreground service", e)
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
        stateObserverJob?.cancel()
        super.onDestroy()
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
            val notification = createNotification(timerRepository.timerState.value)
            startForeground(NOTIFICATION_ID, notification)
            Log.d(TAG, "Started foreground service")
        } catch (e: Exception) {
            Log.e(TAG, "Error starting foreground service", e)
        }
    }

    private fun updateNotification(state: com.sultonuzdev.pft.domain.model.NewTimerState) {
        try {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val notification = createNotification(state)
            notificationManager.notify(NOTIFICATION_ID, notification)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating notification", e)
        }
    }

    private fun createNotification(state: com.sultonuzdev.pft.domain.model.NewTimerState): Notification {
        val title = when (state.currentType) {
            TimerType.POMODORO -> "Focus Time"
            TimerType.SHORT_BREAK -> "Short Break"
            TimerType.LONG_BREAK -> "Long Break"
        }

        val contentText = when (state.timerState) {
            TimerState.RUNNING -> "${state.formattedTime} remaining"
            TimerState.PAUSED -> "Paused - ${state.formattedTime} remaining"
            TimerState.COMPLETED -> when (state.currentType) {
                TimerType.POMODORO -> "Focus session completed!"
                TimerType.SHORT_BREAK -> "Break time over!"
                TimerType.LONG_BREAK -> "Long break completed!"
            }
            else -> "Timer ready"
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
            .setOngoing(state.timerState == TimerState.RUNNING || state.timerState == TimerState.PAUSED)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)

        // Add action buttons based on current state
        when (state.timerState) {
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
        val intent = Intent(this, NewTimerService::class.java).apply {
            this.action = action
        }
        val pendingIntent = PendingIntent.getService(
            this, action.hashCode(), intent, PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Action(iconRes, title, pendingIntent)
    }
}
