package com.sultonuzdev.pft.features.timer.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sultonuzdev.pft.core.util.TimerState
import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.features.timer.domain.repository.StatsRepository
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
 * Complete implementation with:
 * - Proper session saving on timer completion
 * - Statistics integration with SessionRepository
 * - Service state observation
 * - Timer type change handling
 */
@HiltViewModel
class  TimerViewModel @Inject constructor(
    private val getTimerSettingsUseCase: GetTimerSettingsUseCase,
    private val saveTimerSessionUseCase: SaveTimerSessionUseCase,
    private val timerServiceManager: TimerServiceManager,
    private val statsRepository: StatsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TimerUiState())
    val uiState: StateFlow<TimerUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<TimerEffect>()
    val effect: SharedFlow<TimerEffect> = _effect.asSharedFlow()

    private var startTime: LocalDateTime? = null
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
                getTimerSettingsUseCase().collectLatest { settings ->
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
                statsRepository.getTodayStatsFlow().collectLatest { todayStats ->
                    Log.d("TimerViewModel", "Today stats updated: $todayStats")
                    _uiState.update { it.copy(todayStats = todayStats) }
                }
            } catch (e: Exception) {
                Log.e("TimerViewModel", "Error loading today stats", e)
            }
        }

        // Observe all-time stats
        viewModelScope.launch {
            try {
                statsRepository.getAllTimeStatsFlow().collectLatest { allTimeStats ->
                    Log.d("TimerViewModel", "All-time stats updated: $allTimeStats")
                    _uiState.update { it.copy(allTimeStats = allTimeStats) }
                }
            } catch (e: Exception) {
                Log.e("TimerViewModel", "Error loading all-time stats", e)
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
                Log.d("TimerViewModel", "Service state changed: $previousTimerState -> $serviceState")

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
                    // Reset start time for new session
                    startTime = null
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

    fun processIntent(intent: TimerIntent) {
        Log.d("TimerViewModel", "Processing intent: $intent")
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
        Log.d("TimerViewModel", "Timer started at: $startTime for type: ${_uiState.value.currentType}")
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

    private fun stopTimer() {
        Log.d("TimerViewModel", "Stopping timer")
        timerServiceManager.stopTimer()

        // Save incomplete session if it was a Pomodoro
        if (_uiState.value.currentType == TimerType.POMODORO && startTime != null) {
            Log.d("TimerViewModel", "Saving incomplete Pomodoro session")
            savePomodoro(_uiState.value.currentType, false)
        }

        startTime = null
    }

    private fun skipTimer() {
        Log.d("TimerViewModel", "Skipping timer")

        // For tracking purposes, if skipping a Pomodoro, save it as incomplete
        if (_uiState.value.currentType == TimerType.POMODORO &&
            _uiState.value.timerState != TimerState.IDLE &&
            startTime != null
        ) {
            Log.d("TimerViewModel", "Saving skipped Pomodoro session")
            savePomodoro(_uiState.value.currentType, false)
        }

        timerServiceManager.skipTimer()
        startTime = null
    }

    private fun changeTimerType(type: TimerType) {
        Log.d("TimerViewModel", "Changing timer type to: $type")
        // Only call service, let it update the state - service is the single source of truth
        timerServiceManager.changeTimerType(type)
        startTime = null
    }

    private fun handleTimerCompletion(completedType: TimerType) {
        Log.d("TimerViewModel", "Handling completion for type: $completedType")

        viewModelScope.launch {
            // Play sound/vibration based on settings
            try {
                if (_uiState.value.settings.soundEnabled) {
                    _effect.emit(TimerEffect.PlayTimerCompletedSound)
                }

                if (_uiState.value.settings.vibrationEnabled) {
                    _effect.emit(TimerEffect.VibrateDevice)
                }

                // If a Pomodoro completed, show a motivational quote and save session
                if (completedType == TimerType.POMODORO) {
                    Log.d("TimerViewModel", "Pomodoro completed - showing quote and saving session")
                    _effect.emit(TimerEffect.ShowQuote(quotes.random()))
                    savePomodoro(completedType, true) // Save completed session
                } else {
                    Log.d("TimerViewModel", "Break completed - not saving session")
                }

                // Show completion message
                val message = when (completedType) {
                    TimerType.POMODORO -> "Pomodoro completed! Take a break."
                    TimerType.SHORT_BREAK -> "Break's over. Ready for another Pomodoro?"
                    TimerType.LONG_BREAK -> "Long break completed. Great job on your session!"
                }
                _effect.emit(TimerEffect.ShowMessage(message))
            } catch (e: Exception) {
                Log.e("TimerViewModel", "Error handling timer completion", e)
            }
        }
    }

    private fun savePomodoro(timerType: TimerType, completed: Boolean) {
        val currentStartTime = startTime
        if (currentStartTime == null) {
            Log.w("TimerViewModel", "Cannot save session - startTime is null")
            return
        }

        val endTime = LocalDateTime.now()
        val durationMinutes = when (timerType) {
            TimerType.POMODORO -> _uiState.value.settings.pomodoroMinutes
            TimerType.SHORT_BREAK -> _uiState.value.settings.shortBreakMinutes
            TimerType.LONG_BREAK -> _uiState.value.settings.longBreakMinutes
        }

        Log.d("TimerViewModel", "Saving session: type=$timerType, duration=${durationMinutes}min, completed=$completed, start=$currentStartTime, end=$endTime")

        viewModelScope.launch {
            try {
                saveTimerSessionUseCase(
                    type = timerType,
                    durationMinutes = durationMinutes,
                    completed = completed,
                    startTime = currentStartTime,
                    endTime = endTime
                )
                Log.d("TimerViewModel", "Session saved successfully")
            } catch (e: Exception) {
                Log.e("TimerViewModel", "Error saving session", e)
                // Emit error effect to show user
                _effect.emit(TimerEffect.ShowMessage("Failed to save session"))
            }
        }
    }

    override fun onCleared() {
        Log.d("TimerViewModel", "ViewModel cleared - unbinding service")
        super.onCleared()
        timerServiceManager.unbindService()
    }
}