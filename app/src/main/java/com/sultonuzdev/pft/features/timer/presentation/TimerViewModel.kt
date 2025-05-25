package com.sultonuzdev.pft.features.timer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sultonuzdev.pft.core.util.TimerState
import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.features.timer.domain.usecase.GetTimerSettingsUseCase
import com.sultonuzdev.pft.features.timer.domain.usecase.SaveTimerSessionUseCase
import com.sultonuzdev.pft.features.timer.presentation.utils.TimerEffect
import com.sultonuzdev.pft.features.timer.presentation.utils.TimerIntent
import com.sultonuzdev.pft.features.timer.presentation.utils.TimerUiState
import com.sultonuzdev.pft.service.TimerServiceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * ViewModel for the Timer screen implementing MVI pattern
 * Now observes TimerService state instead of maintaining its own timer
 * FIXED: Timer type change issue resolved
 */
@HiltViewModel
class TimerViewModel @Inject constructor(
    private val getTimerSettingsUseCase: GetTimerSettingsUseCase,
    private val saveTimerSessionUseCase: SaveTimerSessionUseCase,
    private val timerServiceManager: TimerServiceManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(TimerUiState())
    val uiState: StateFlow<TimerUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<TimerEffect>()
    val effect: SharedFlow<TimerEffect> = _effect.asSharedFlow()

    private var startTime: LocalDateTime? = null
    private var previousTimerState: TimerState = TimerState.IDLE

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
    }

    private fun loadSettings() {
        viewModelScope.launch {
            getTimerSettingsUseCase().collectLatest { settings ->
                _uiState.update { currentState ->
                    // Always update settings, but don't override service state
                    currentState.copy(settings = settings)
                }
            }
        }
    }

    private fun observeServiceState() {
        // Observe service connection
        viewModelScope.launch {
            timerServiceManager.isConnected.collectLatest { connected ->
                if (connected) {
                    // Start observing all service state flows once connected
                    observeAllServiceFlows()
                }
            }
        }
    }

    private fun observeAllServiceFlows() {
        // Observe timer state
        viewModelScope.launch {
            timerServiceManager.timerState.collectLatest { serviceState ->
                val isCompleted = serviceState == TimerState.COMPLETED

                _uiState.update { it.copy(timerState = serviceState) }

                // Handle timer completion
                if (isCompleted && previousTimerState == TimerState.RUNNING) {
                    handleTimerCompletion()
                }

                previousTimerState = serviceState
            }
        }

        // Observe current timer type - SERVICE IS AUTHORITY
        viewModelScope.launch {
            timerServiceManager.currentTimerType.collectLatest { type ->
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

        // Observe completed pomodoros
        viewModelScope.launch {
            timerServiceManager.completedPomodoros.collectLatest { completed ->
                _uiState.update { it.copy(completedPomodoros = completed) }
            }
        }
    }

    fun processIntent(intent: TimerIntent) {
        when (intent) {
            is TimerIntent.StartTimer -> startTimer()
            is TimerIntent.PauseTimer -> pauseTimer()
            is TimerIntent.ResumeTimer -> resumeTimer()
            is TimerIntent.StopTimer -> stopTimer()
            is TimerIntent.SkipTimer -> skipTimer()
            is TimerIntent.ChangeTimerType -> changeTimerType(intent.type)
        }
    }

    private fun startTimer() {
        startTime = LocalDateTime.now()
        timerServiceManager.startTimer(
            _uiState.value.currentType,
            _uiState.value.settings
        )
    }

    private fun pauseTimer() {
        timerServiceManager.pauseTimer()
    }

    private fun resumeTimer() {
        timerServiceManager.resumeTimer()
    }

    private fun stopTimer() {
        timerServiceManager.stopTimer()

        // Save incomplete session if it was a Pomodoro
        if (_uiState.value.currentType == TimerType.POMODORO && startTime != null) {
            saveSession(false)
        }

        startTime = null
    }

    private fun skipTimer() {
        // For tracking purposes, if skipping a Pomodoro, save it as incomplete
        if (_uiState.value.currentType == TimerType.POMODORO &&
            _uiState.value.timerState != TimerState.IDLE &&
            startTime != null) {
            saveSession(false)
        }

        timerServiceManager.skipTimer()
        startTime = null
    }

    private fun changeTimerType(type: TimerType) {
        // FIXED: Only call service, let it update the state
        // Don't update local state here - service is the single source of truth
        timerServiceManager.changeTimerType(type)
        startTime = null
    }

    private fun handleTimerCompletion() {
        val currentType = _uiState.value.currentType

        viewModelScope.launch {
            // Play sound/vibration
            if (_uiState.value.settings.soundEnabled) {
                _effect.emit(TimerEffect.PlayTimerCompletedSound)
            }

            if (_uiState.value.settings.vibrationEnabled) {
                _effect.emit(TimerEffect.VibrateDevice)
            }

            // If a Pomodoro completed, show a motivational quote
            if (currentType == TimerType.POMODORO) {
                _effect.emit(TimerEffect.ShowQuote(quotes.random()))
                saveSession(true)
            }

            // Show completion message
            val message = when (currentType) {
                TimerType.POMODORO -> "Pomodoro completed! Take a break."
                TimerType.SHORT_BREAK -> "Break's over. Ready for another Pomodoro?"
                TimerType.LONG_BREAK -> "Long break completed. Great job on your session!"
            }
            _effect.emit(TimerEffect.ShowMessage(message))
        }

        startTime = null
    }

    private fun saveSession(completed: Boolean) {
        val currentStartTime = startTime ?: return
        val endTime = LocalDateTime.now()
        val currentType = _uiState.value.currentType
        val durationMinutes = when (currentType) {
            TimerType.POMODORO -> _uiState.value.settings.pomodoroMinutes
            TimerType.SHORT_BREAK -> _uiState.value.settings.shortBreakMinutes
            TimerType.LONG_BREAK -> _uiState.value.settings.longBreakMinutes
        }

        viewModelScope.launch {
            saveTimerSessionUseCase(
                type = currentType,
                durationMinutes = durationMinutes,
                completed = completed,
                startTime = currentStartTime,
                endTime = endTime
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerServiceManager.unbindService()
    }
}