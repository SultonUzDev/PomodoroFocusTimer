package com.sultonuzdev.pft.presentation.timer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sultonuzdev.pft.core.util.TimerState
import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.data.media.PomodoroTimerMediaController
import com.sultonuzdev.pft.domain.repository.SettingsRepository
import com.sultonuzdev.pft.domain.usecase.PomodoroUseCases
import com.sultonuzdev.pft.presentation.service.TimerServiceManager
import com.sultonuzdev.pft.presentation.timer.contract.TimerMviContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Timer screen implementing MVI pattern
 * Complete implementation with:
 * - Proper session saving on timer completion
 * - Statistics integration with SessionRepository
 * - Service state observation
 * - Timer type change handling
 */
@HiltViewModel
class TimerViewModel @Inject constructor(
    private val timerServiceManager: TimerServiceManager,
    private val pomodoroUseCases: PomodoroUseCases,
    private val settingsRepository: SettingsRepository,
    private val mediaController: PomodoroTimerMediaController,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TimerMviContract.TimerUiState())
    val uiState: StateFlow<TimerMviContract.TimerUiState> = _uiState.asStateFlow()

    private val _effect = Channel<TimerMviContract.TimerEffect>()
    val effect: Flow<TimerMviContract.TimerEffect> = _effect.receiveAsFlow()

    private var previousTimerState: TimerState = TimerState.IDLE
    private var completedTimerType: TimerType? = null // Track what type completed

    // List of motivational quotes
    private val quotes = listOf(
        "The secret to getting ahead is getting started.",
        "Focus on being productive instead of busy.",
        "The way to get started is to quit talking and begin doing.",
        "It's not about having time, it's about making time.",
        "You don't have to be great to start, but you have to start to be great.",
        "Productivity is never an accident. It is always the result of commitment to excellence."
    )

    init {
        // Bind to service and start observing its state
        timerServiceManager.bindService()
        observeServiceState()
        loadSettings()
        loadStatistics()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            try {
                settingsRepository.getDefaultSettings().collectLatest { settings ->
                    _uiState.update { currentState ->
                        // Always update settings, but don't override service state
                        currentState.copy(settings = settings)
                    }
                }
            } catch (e: Exception) {
                Log.e("TimerViewModel", "Error loading settings", e)
            }
        }
    }

    private fun loadStatistics() {
        // Observe today's stats
        viewModelScope.launch {
            try {
                pomodoroUseCases.getTodayPomodoro().collectLatest { todayStats ->
                    Log.d("TimerViewModel", "Today stats updated: $todayStats")
                    _uiState.update { it.copy(todayStats = todayStats) }
                }
            } catch (e: Exception) {
                Log.e("TimerViewModel", "Error loading today stats", e)
            }
        }


    }

    private fun observeServiceState() {
        // Observe service connection
        viewModelScope.launch {
            timerServiceManager.isConnected.collectLatest { connected ->
                Log.d("TimerViewModel", "Service connection state: $connected")
                if (connected) {
                    // Start observing all service state flows once connected
                    observeAllServiceFlows()
                }
            }
        }
    }

    private fun observeAllServiceFlows() {
        // Observe timer state - Enhanced completion detection
        viewModelScope.launch {
            timerServiceManager.timerState.collectLatest { serviceState ->
                Log.d(
                    "TimerViewModel",
                    "Service state changed: $previousTimerState -> $serviceState"
                )

                // Detect when timer completes
                if (serviceState == TimerState.COMPLETED && previousTimerState == TimerState.RUNNING) {
                    // Store the type that completed before it changes
                    completedTimerType = _uiState.value.currentType
                    Log.d("TimerViewModel", "Timer completed: $completedTimerType")

                    // Handle completion (this includes saving session)
                    handleTimerCompletion(completedTimerType!!)
                }

                // Detect when timer goes from COMPLETED back to IDLE (auto-transition)
                if (serviceState == TimerState.IDLE && previousTimerState == TimerState.COMPLETED) {
                    Log.d("TimerViewModel", "Timer auto-transitioned to next type")
                }

                _uiState.update { it.copy(timerState = serviceState) }
                previousTimerState = serviceState
            }
        }

        // Observe current timer type - SERVICE IS AUTHORITY
        viewModelScope.launch {
            timerServiceManager.currentTimerType.collectLatest { type ->
                Log.d("TimerViewModel", "Timer type changed to: $type")
                _uiState.update { it.copy(currentType = type) }
            }
        }

        // Observe remaining time - SERVICE IS AUTHORITY
        viewModelScope.launch {
            timerServiceManager.remainingTimeMillis.collectLatest { remaining ->
                _uiState.update { it.copy(remainingTimeMillis = remaining) }
            }
        }

        // Observe total time - SERVICE IS AUTHORITY
        viewModelScope.launch {
            timerServiceManager.totalTimeMillis.collectLatest { total ->
                _uiState.update { it.copy(totalTimeMillis = total) }
            }
        }

        // Observe progress fraction - SERVICE IS AUTHORITY
        viewModelScope.launch {
            timerServiceManager.progressFraction.collectLatest { progress ->
                _uiState.update { it.copy(progressFraction = progress) }
            }
        }

        // Observe formatted time - SERVICE IS AUTHORITY
        viewModelScope.launch {
            timerServiceManager.formattedTime.collectLatest { time ->
                _uiState.update { it.copy(formattedTime = time) }
            }
        }

        // Observe current session pomodoros from service
        viewModelScope.launch {
            try {
                timerServiceManager.currentSessionPomodoros.collectLatest { sessionPomodoros ->
                    Log.d("TimerViewModel", "Current session pomodoros: $sessionPomodoros")
                    _uiState.update { it.copy(currentSessionPomodoros = sessionPomodoros) }
                }
            } catch (e: Exception) {
                Log.e("TimerViewModel", "Error observing session pomodoros", e)
                // Set default value if service doesn't provide this flow
                _uiState.update { it.copy(currentSessionPomodoros = 0) }
            }
        }

    }

    fun processIntent(intent: TimerMviContract.TimerIntent) {
        viewModelScope.launch {

            Log.d("TimerViewModel", "Processing intent: $intent")
            when (intent) {
                is TimerMviContract.TimerIntent.StartTimer -> startTimer()
                is TimerMviContract.TimerIntent.PauseTimer -> pauseTimer()
                is TimerMviContract.TimerIntent.ResumeTimer -> resumeTimer()
                is TimerMviContract.TimerIntent.FinishTimer -> finishTimer()
                is TimerMviContract.TimerIntent.SkipTimer -> skipTimer()
                is TimerMviContract.TimerIntent.SetTimerStyle -> {
                    _uiState.update { it.copy(timerStyle = intent.timerStyle) }
                }

                TimerMviContract.TimerIntent.NavigateToSettings -> {
                    _effect.send(TimerMviContract.TimerEffect.NavigateToSettings)
                }

                TimerMviContract.TimerIntent.NavigateToStats -> {
                    _effect.send(TimerMviContract.TimerEffect.NavigateToStats)
                }

                TimerMviContract.TimerIntent.NavigateToTimerStyle -> {
                    _effect.send(TimerMviContract.TimerEffect.NavigateToTimerStyle)
                }
            }
        }
    }

    private fun startTimer() {
        Log.d("TimerViewModel", "Starting timer for type: ${_uiState.value.currentType}")
        timerServiceManager.startTimer(
            _uiState.value.currentType,
            _uiState.value.settings
        )
    }

    private fun pauseTimer() {
        Log.d("TimerViewModel", "Pausing timer")
        timerServiceManager.pauseTimer()
    }

    private fun resumeTimer() {
        Log.d("TimerViewModel", "Resuming timer")
        timerServiceManager.resumeTimer()
    }

    private fun finishTimer() {
        Log.d("TimerViewModel", "Stopping timer")
        timerServiceManager.stopTimer()
    }

    private fun skipTimer() {
        Log.d("TimerViewModel", "Skipping timer")
        timerServiceManager.skipTimer()
    }


    private fun handleTimerCompletion(completedType: TimerType) {
        Log.d("TimerViewModel", "Handling completion for type: $completedType")

        viewModelScope.launch {
            // Play sound/vibration based on settings
            try {
                if (_uiState.value.settings.soundEnabled) {
                    mediaController.playSound()
                }

                if (_uiState.value.settings.vibrationEnabled) {
                    mediaController.vibrateDevice()
                }

                // If a Pomodoro completed, show a motivational quote
                // Note: Session saving is handled by TimerService
                if (completedType == TimerType.POMODORO) {
                    Log.d("TimerViewModel", "Pomodoro completed - showing quote")
                    _effect.send(TimerMviContract.TimerEffect.ShowQuote(quotes.random()))
                } else {
                    Log.d("TimerViewModel", "Break completed")
                }

                // Show completion message
                val message = when (completedType) {
                    TimerType.POMODORO -> "Pomodoro completed! Take a break."
                    TimerType.SHORT_BREAK -> "Break's over. Ready for another Pomodoro?"
                    TimerType.LONG_BREAK -> "Long break completed. Great job on your session!"
                }
                _effect.send(TimerMviContract.TimerEffect.ShowMessage(message))
            } catch (e: Exception) {
                Log.e("TimerViewModel", "Error handling timer completion", e)
            }
        }
    }

    override fun onCleared() {
        Log.d("TimerViewModel", "ViewModel cleared")
        super.onCleared()

        // Only unbind service if timer is not running
        // If timer is running, keep service bound to maintain foreground service
        val currentState = _uiState.value.timerState
        if (currentState == TimerState.IDLE) {
            Log.d("TimerViewModel", "Timer idle - unbinding service")
            timerServiceManager.unbindService()
        } else {
            Log.d("TimerViewModel", "Timer active ($currentState) - keeping service bound")
        }
    }
}