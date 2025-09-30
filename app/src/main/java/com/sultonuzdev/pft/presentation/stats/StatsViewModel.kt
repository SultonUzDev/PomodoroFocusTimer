package com.sultonuzdev.pft.presentation.stats

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sultonuzdev.pft.domain.usecase.PomodoroUseCases
import com.sultonuzdev.pft.presentation.stats.utils.StatsEffect
import com.sultonuzdev.pft.presentation.stats.utils.StatsIntent
import com.sultonuzdev.pft.presentation.stats.utils.StatsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

/**
 * ViewModel for the Stats screen implementing MVI pattern
 */
@HiltViewModel
class StatsViewModel @Inject constructor(
    private val pomodoroUseCases: PomodoroUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatsUiState())
    val uiState: StateFlow<StatsUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<StatsEffect>()
    val effect: SharedFlow<StatsEffect> = _effect.asSharedFlow()

    init {
        // Set initial week start date to the beginning of the current week
        val today = LocalDate.now()
        val weekStart = pomodoroUseCases.getStartOfWeek(today)

        _uiState.update {
            it.copy(
                selectedDate = today, weekStartDate = weekStart
            )
        }

        // Load initial data
        loadAllStats()
    }

    /**
     * Process user intents from the UI
     */
    fun processIntent(intent: StatsIntent) {
        when (intent) {
            is StatsIntent.SelectDate -> {
                _uiState.update { it.copy(selectedDate = intent.date) }
                loadDailyStats(intent.date)
            }

            is StatsIntent.LoadWeeklyStats -> {
                val weekStart = pomodoroUseCases.getStartOfWeek(_uiState.value.selectedDate)
                _uiState.update { it.copy(weekStartDate = weekStart) }
                loadWeeklyStats(weekStart)
            }

            is StatsIntent.NavigateToPreviousWeek -> {
                val newWeekStart = _uiState.value.weekStartDate.minusDays(7)
                _uiState.update {
                    it.copy(
                        weekStartDate = newWeekStart, selectedDate = newWeekStart
                    )
                }
                loadWeeklyStats(newWeekStart)
                loadDailyStats(newWeekStart)
            }

            is StatsIntent.NavigateToNextWeek -> {
                val newWeekStart = _uiState.value.weekStartDate.plusDays(7)
                val today = LocalDate.now()

                // Don't navigate past the current week
                if (newWeekStart.isBefore(today) || newWeekStart.isEqual(
                        pomodoroUseCases.getStartOfWeek(
                            today
                        )
                    )
                ) {
                    _uiState.update {
                        it.copy(
                            weekStartDate = newWeekStart, selectedDate = newWeekStart
                        )
                    }
                    loadWeeklyStats(newWeekStart)
                    loadDailyStats(newWeekStart)
                } else {
                    viewModelScope.launch {
                        _effect.emit(StatsEffect.ShowMessage("Cannot navigate to future weeks"))
                    }
                }
            }

            is StatsIntent.RefreshStats -> {
                loadAllStats()
                viewModelScope.launch {
                    _effect.emit(StatsEffect.ShowMessage("Stats refreshed"))
                }
            }

        }
    }

    /**
     * Load all statistics data
     */
    private fun loadAllStats() {
        loadDailyStats(_uiState.value.selectedDate)
        loadWeeklyStats(_uiState.value.weekStartDate)
        calculateAggregateStats()
    }

    /**
     * Load stats for the selected day
     */
    private fun loadDailyStats(date: LocalDate) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                pomodoroUseCases.getDailyStats(date).collectLatest { stats ->
                    _uiState.update {
                        it.copy(
                            dailyStats = stats, isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false, errorMessage = "Failed to load daily stats: ${e.message}"
                    )
                }
                _effect.emit(StatsEffect.ShowMessage("Error loading daily stats"))
            }
        }
    }

    /**
     * Load stats for the current week
     */
    private fun loadWeeklyStats(startDate: LocalDate) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                pomodoroUseCases.getWeeklyAvgStats(startDate).collectLatest { weekStats ->
                    _uiState.update {
                        it.copy(
                            weeklyStats = weekStats, isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to load weekly stats: ${e.message}"
                    )
                }
                _effect.emit(StatsEffect.ShowMessage("Error loading weekly stats"))
            }
        }
    }


    /**
     * Calculate aggregate statistics
     */
    private fun calculateAggregateStats() {
        viewModelScope.launch {
            try {
                // Use StatsRepository methods for aggregate calculations
                val totalPomodoros =
                    pomodoroUseCases.getCompletedPomodoros.invoke(uiState.value.selectedDate)
                        .first()
                val totalMinutes = pomodoroUseCases.getTotalFocusTime.invoke().first()

                // Calculate average daily focus minutes (only for days that have sessions)
                val weeklyStats =
                    pomodoroUseCases.getWeeklyAvgStats.invoke(date = LocalDate.now())
                        .first()
                
                val daysWithSessions = weeklyStats.filter { it.totalFocusMinutes > 0 }.size
                val avgDailyMinutes = if (daysWithSessions > 0) {
                    weeklyStats.sumOf { it.totalFocusMinutes } / daysWithSessions
                } else 0

                _uiState.update {
                    it.copy(
                        totalCompletedPomodoros = totalPomodoros,
                        totalFocusMinutes = totalMinutes,
                        averageDailyFocusMinutes = avgDailyMinutes
                    )
                }
            } catch (e: Exception) {
                Log.d("mlog", "Error: ${e.localizedMessage}")
                _effect.emit(StatsEffect.ShowMessage("Error calculating statistics"))
            }
        }
    }


}