package com.sultonuzdev.pft.presentation.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat
import com.sultonuzdev.pft.core.util.TimerState
import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.domain.model.TimerSettings
import com.sultonuzdev.pft.presentation.service.TimerServiceConstants.ACTION_START
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages connection to the TimerService and provides access to its state flows
 * Updated to handle manual timer type changes
 */
@Singleton
class TimerServiceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var timerService: TimerService? = null
    private var isServiceBound = false

    // Connection state flow
    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    // Service connection object
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG, "Service connected")
            val binder = service as TimerService.TimerBinder
            timerService = binder.getService()
            isServiceBound = true
            _isConnected.value = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "Service disconnected")
            timerService = null
            isServiceBound = false
            _isConnected.value = false
        }
    }

    // Flow accessors for timer state - these now properly handle null service
    val timerState: Flow<TimerState>
        get() = timerService?.timerState ?: run {
            Log.w(TAG, "Service not connected, returning empty flow for timerState")
            emptyFlow()
        }

    val currentTimerType: Flow<TimerType>
        get() = timerService?.currentTimerType ?: run {
            Log.w(TAG, "Service not connected, returning empty flow for currentTimerType")
            emptyFlow()
        }

    val remainingTimeMillis: Flow<Long>
        get() = timerService?.remainingTimeMillis ?: run {
            Log.w(TAG, "Service not connected, returning empty flow for remainingTimeMillis")
            emptyFlow()
        }

    val totalTimeMillis: Flow<Long>
        get() = timerService?.totalTimeMillis ?: run {
            Log.w(TAG, "Service not connected, returning empty flow for totalTimeMillis")
            emptyFlow()
        }

    val progressFraction: Flow<Float>
        get() = timerService?.progressFraction ?: run {
            Log.w(TAG, "Service not connected, returning empty flow for progressFraction")
            emptyFlow()
        }

    val formattedTime: Flow<String>
        get() = timerService?.formattedTime ?: run {
            Log.w(TAG, "Service not connected, returning empty flow for formattedTime")
            emptyFlow()
        }

    val completedPomodoros: Flow<Int>
        get() = timerService?.completedPomodoros ?: run {
            Log.w(TAG, "Service not connected, returning empty flow for completedPomodoros")
            emptyFlow()
        }

    val completedSessions: Flow<Int>
        get() = timerService?.completedSessions ?: run {
            Log.w(TAG, "Service not connected, returning empty flow for completedSessions")
            emptyFlow()
        }


    // Add these new flow accessors to your TimerServiceManager.kt:


    val currentSessionPomodoros: Flow<Int>
        get() = timerService?.currentSessionPomodoros ?: run {
            Log.w(TAG, "Service not connected, returning empty flow for currentSessionPomodoros")
            emptyFlow()
        }

    val totalSessions: Flow<Int>
        get() = timerService?.totalSessions ?: run {
            Log.w(TAG, "Service not connected, returning empty flow for totalSessions")
            emptyFlow()
        }


    /**
     * Binds to the TimerService
     */
    fun bindService() {
        if (!isServiceBound) {
            Log.d(TAG, "Binding to timer service")
            val intent = Intent(context, TimerService::class.java)
            try {
                val result = context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
                if (!result) {
                    Log.e(TAG, "Failed to bind to service")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error binding to service", e)
            }
        } else {
            Log.d(TAG, "Service already bound")
        }
    }

    /**
     * Unbinds from the TimerService
     */
    fun unbindService() {
        if (isServiceBound) {
            Log.d(TAG, "Unbinding from timer service")
            try {
                context.unbindService(serviceConnection)
                isServiceBound = false
                _isConnected.value = false
                timerService = null
            } catch (e: Exception) {
                Log.e(TAG, "Error unbinding from service", e)
            }
        } else {
            Log.d(TAG, "Service not bound, nothing to unbind")
        }
    }

    /**
     * Starts the timer service with the specified timer type and duration
     * Duration is now calculated from settings in the service
     */
    fun startTimer(timerType: TimerType, settings: TimerSettings) {
        Log.d(TAG, "Starting timer: type=$timerType")

        val durationMillis = when (timerType) {
            TimerType.POMODORO -> settings.pomodoroMinutes * 60 * 1000L
            TimerType.SHORT_BREAK -> settings.shortBreakMinutes * 60 * 1000L
            TimerType.LONG_BREAK -> settings.longBreakMinutes * 60 * 1000L
        }

        // Start the service via intent (this will work even if not bound)
        val intent = Intent(context, TimerService::class.java).apply {
            action = ACTION_START
            putExtra(TimerServiceConstants.EXTRA_TIMER_TYPE, timerType)
            putExtra(TimerServiceConstants.EXTRA_TIMER_DURATION, durationMillis)
        }

        try {
            startForegroundService(intent)

            // Also call directly if service is bound for immediate response
            timerService?.startTimer(timerType, durationMillis)
        } catch (e: Exception) {
            Log.e(TAG, "Error starting timer", e)
        }
    }

    /**
     * Changes timer type manually (only when timer is idle)
     */
    fun changeTimerType(timerType: TimerType) {
        Log.d(TAG, "Changing timer type to: $timerType")

        // Call service directly if bound
        timerService?.changeTimerTypeManually(timerType)
    }

    /**
     * Gets current settings from the service
     */
    fun getCurrentSettings(): TimerSettings? {
        return timerService?.getCurrentSettings()
    }

    /**
     * Pauses the current timer
     */
    fun pauseTimer() {
        Log.d(TAG, "Pausing timer")

        // Call service directly if bound
        timerService?.pauseTimer()

        // Also send intent to ensure service receives command
        val intent = Intent(context, TimerService::class.java).apply {
            action = TimerServiceConstants.ACTION_PAUSE
        }

        try {
            context.startService(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Error pausing timer", e)
        }
    }

    /**
     * Resumes the paused timer
     */
    fun resumeTimer() {
        Log.d(TAG, "Resuming timer")

        // Call service directly if bound
        timerService?.resumeTimer()

        // Also send intent to ensure service receives command
        val intent = Intent(context, TimerService::class.java).apply {
            action = TimerServiceConstants.ACTION_RESUME
        }

        try {
            context.startService(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Error resuming timer", e)
        }
    }

    /**
     * Stops the current timer and stops the foreground service
     */
    fun stopTimer() {
        Log.d(TAG, "Stopping timer")

        // Call service directly if bound
        timerService?.stopTimer()

        // Also send stop intent to ensure service stops
        val intent = Intent(context, TimerService::class.java).apply {
            action = TimerServiceConstants.ACTION_STOP
        }

        try {
            context.startService(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping timer", e)
        }
    }

    /**
     * Skips the current timer, marking it as completed
     */
    fun skipTimer() {
        Log.d(TAG, "Skipping timer")

        // Call service directly if bound
        timerService?.skipTimer()

        // Also send intent to ensure service receives command
        val intent = Intent(context, TimerService::class.java).apply {
            action= TimerServiceConstants.ACTION_SKIP
        }

        try {
            context.startService(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Error skipping timer", e)
        }
    }

    /**
     * Helper method to start the foreground service
     */
    private fun startForegroundService(intent: Intent) {
        try {
            ContextCompat.startForegroundService(context, intent)
        } catch (e: Exception) {
            Log.e(TAG, "Error starting foreground service", e)
        }
    }

    /**
     * Check if service is currently connected
     */
    fun isServiceConnected(): Boolean = isServiceBound && timerService != null

    companion object {
        private const val TAG = "TimerServiceManager"
    }
}