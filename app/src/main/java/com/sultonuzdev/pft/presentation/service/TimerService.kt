package com.sultonuzdev.pft.presentation.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.sultonuzdev.pft.MainActivity
import com.sultonuzdev.pft.R
import com.sultonuzdev.pft.core.util.TimerState
import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.domain.repository.TimerRepository
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
class TimerService : Service() {

    @Inject
    lateinit var timerRepository: TimerRepository

    private val serviceScope = CoroutineScope(Dispatchers.Main)
    private var stateObserverJob: Job? = null

    private var lastNotificationUpdateTime: Long = 0L
    private val NOTIFICATION_UPDATE_INTERVAL = 1000L // 1 seconds




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

        if (intent?.action == ACTION_START) {
            try {
                startForegroundService()
            } catch (e: Exception) {
                Log.e(TAG, "Failed to start foreground service", e)
            }
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
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
        } catch (e: Exception) {
            Log.e(TAG, "Error creating notification channel", e)
        }
    }

    private fun startForegroundService() {
        try {
            val notification = createNotification(timerRepository.timerState.value)
            startForeground(NOTIFICATION_ID, notification)
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

        return builder.build()
    }
}
