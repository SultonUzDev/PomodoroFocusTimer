package com.sultonuzdev.pft.presentation.timer

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sultonuzdev.pft.core.util.TimerState
import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.data.media.PomodoroTimerMediaController
import com.sultonuzdev.pft.domain.repository.SettingsRepository
import com.sultonuzdev.pft.domain.repository.TimerRepository
import com.sultonuzdev.pft.domain.usecase.PomodoroUseCases
import com.sultonuzdev.pft.presentation.service.TimerService
import com.sultonuzdev.pft.presentation.service.TimerServiceConstants.ACTION_START
import com.sultonuzdev.pft.presentation.timer.contract.TimerMviContract
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
 * New ViewModel that uses TimerRepository directly
 * No service manager needed - repository is the single source of truth
 * Service runs in parallel for foreground notifications
 */
@HiltViewModel
class TimerViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val timerRepository: TimerRepository,
    private val pomodoroUseCases: PomodoroUseCases,
    private val settingsRepository: SettingsRepository,
    private val mediaController: PomodoroTimerMediaController,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TimerMviContract.TimerUiState())
    val uiState: StateFlow<TimerMviContract.TimerUiState> = _uiState.asStateFlow()

    private val _effect = Channel<TimerMviContract.TimerEffect>()
    val effect: Flow<TimerMviContract.TimerEffect> = _effect.receiveAsFlow()

    private var previousTimerState: TimerState = TimerState.IDLE
    private var completedTimerType: TimerType? = null

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
        loadStatistics()
        observeRepositoryState()
        observeSettings()
        loadInitialTimerStyle()
    }

    private fun observeRepositoryState() {
        viewModelScope.launch {
            timerRepository.timerState.collectLatest { repoState ->
                Log.d(
                    TAG,
                    "Repository state changed: ${repoState.timerState}, type: ${repoState.currentType}"
                )

                // Detect when timer completes
                if (repoState.timerState == TimerState.COMPLETED && previousTimerState == TimerState.RUNNING) {
                    completedTimerType = repoState.currentType
                    Log.d(TAG, "Timer completed: $completedTimerType")
                    handleTimerCompletion(completedTimerType!!)
                }

                // Map repository state to UI state
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        currentType = repoState.currentType,
                        timerState = repoState.timerState,
                        totalTimeMillis = repoState.totalTimeMillis,
                        remainingTimeMillis = repoState.remainingTimeMillis,
                        formattedTime = repoState.formattedTime,
                        currentTimeMillis = repoState.currentTimeMillis,
                        currentSessionPomodoros = repoState.currentSessionPomodoros,
                        settings = repoState.settings,
                        // Calculate progress fraction
                        progressFraction = if (repoState.totalTimeMillis > 0) {
                            1f - (repoState.remainingTimeMillis.toFloat() / repoState.totalTimeMillis.toFloat())
                        } else {
                            0f
                        }
                    )
                }

                previousTimerState = repoState.timerState
            }
        }
    }

    private fun observeSettings() {
        Log.d(TAG, "Observing settings")
        viewModelScope.launch {
            try {
                settingsRepository.getDefaultSettings().collectLatest { settings ->
                    _uiState.update { it.copy(settings = settings) }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error observing settings", e)
            }
        }
    }

    private fun loadInitialTimerStyle() {
        viewModelScope.launch {
            try {
                settingsRepository.getTimerStyle().collect { timerStyle ->
                    Log.d(TAG, "Initial timer style loaded: $timerStyle")
                    _uiState.update { it.copy(timerStyle = timerStyle) }
                    return@collect // Only load once, don't keep collecting
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading initial timer style", e)
            }
        }
    }

    private suspend fun reloadTimerStyleFromRepository() {
        try {
            settingsRepository.getTimerStyle().collect { timerStyle ->
                Log.d(TAG, "Reloaded timer style from repository: $timerStyle")
                _uiState.update { it.copy(timerStyle = timerStyle) }
                return@collect // Only read once
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error reloading timer style", e)
        }
    }

    private fun loadStatistics() {
        viewModelScope.launch {
            try {
                pomodoroUseCases.getTodayPomodoro().collectLatest { todayStats ->
                    Log.d(TAG, "Today stats updated: $todayStats")
                    _uiState.update { it.copy(todayStats = todayStats) }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading today stats", e)
            }
        }
    }

    fun processIntent(intent: TimerMviContract.TimerIntent) {
        viewModelScope.launch {
            Log.d(TAG, "Processing intent: $intent")
            when (intent) {
                TimerMviContract.TimerIntent.LoadSettings -> {
                    // Reload timer style from repository when navigating back
                    reloadTimerStyleFromRepository()
                }

                is TimerMviContract.TimerIntent.StartTimer -> startTimer()
                is TimerMviContract.TimerIntent.PauseTimer -> pauseTimer()
                is TimerMviContract.TimerIntent.ResumeTimer -> resumeTimer()
                is TimerMviContract.TimerIntent.FinishTimer -> finishTimer()
                is TimerMviContract.TimerIntent.SkipTimer -> skipTimer()

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

    private suspend fun startTimer() {
        Log.d(TAG, "Starting timer for type: ${_uiState.value.currentType}")

        startForegroundService()

        timerRepository.startTimer()
    }

    private fun startForegroundService() {
        try {
            val intent = Intent(context, TimerService::class.java).apply {
                action = ACTION_START
            }
            ContextCompat.startForegroundService(context, intent)
            Log.d(TAG, "Started foreground service for notifications")
        } catch (e: Exception) {
            Log.e(TAG, "Error starting foreground service", e)
            // Show error to user
            viewModelScope.launch {
                _effect.send(
                    TimerMviContract.TimerEffect.ShowMessage(
                        "Failed to start foreground service: ${e.message}"
                    )
                )
            }
        }
    }

    private suspend fun pauseTimer() {
        Log.d(TAG, "Pausing timer")
        // Service will automatically observe state change and update notification
        timerRepository.pauseTimer()
    }

    private suspend fun resumeTimer() {
        Log.d(TAG, "Resuming timer")
        // Service will automatically observe state change and update notification
        timerRepository.resumeTimer()
    }

    private suspend fun finishTimer() {
        Log.d(TAG, "Finishing timer")
        // Service will automatically observe state change and update notification
        timerRepository.finishTimer()
    }

    private suspend fun skipTimer() {
        Log.d(TAG, "Skipping timer")
        // Service will automatically observe state change and update notification
        timerRepository.skipTimer()
    }

    private fun handleTimerCompletion(completedType: TimerType) {
        Log.d(TAG, "Handling completion for type: $completedType")

        viewModelScope.launch {
            try {
                // Play sound/vibration based on settings
                if (_uiState.value.settings.soundEnabled) {
                    mediaController.playSound()
                }

                if (_uiState.value.settings.vibrationEnabled) {
                    mediaController.vibrateDevice()
                }

                // If a Pomodoro completed, show a motivational quote
                if (completedType == TimerType.POMODORO) {
                    Log.d(TAG, "Pomodoro completed - showing quote")
                    _effect.send(TimerMviContract.TimerEffect.ShowQuote(quotes.random()))
                } else {
                    Log.d(TAG, "Break completed")
                }

                // Show completion message
                val message = when (completedType) {
                    TimerType.POMODORO -> "Pomodoro completed! Take a break."
                    TimerType.SHORT_BREAK -> "Break's over. Ready for another Pomodoro?"
                    TimerType.LONG_BREAK -> "Long break completed. Great job on your session!"
                }
                _effect.send(TimerMviContract.TimerEffect.ShowMessage(message))
            } catch (e: Exception) {
                Log.e(TAG, "Error handling timer completion", e)
            }
        }
    }

    companion object Companion {
        private const val TAG = "TimerViewModel"
    }
}
