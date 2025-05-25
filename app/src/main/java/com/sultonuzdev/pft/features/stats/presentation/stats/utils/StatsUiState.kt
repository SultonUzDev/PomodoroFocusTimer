package com.sultonuzdev.pft.features.stats.presentation.stats.utils


import com.sultonuzdev.pft.core.ui.utils.UiState
import com.sultonuzdev.pft.features.stats.domain.model.DailyStats
import java.time.LocalDate

/**
 * Represents the UI state for the Stats screen
 */
data class StatsUiState(
    /**
     * The currently selected date for daily statistics
     */
    val selectedDate: LocalDate = LocalDate.now(),

    /**
     * The start date of the current week being viewed
     */
    val weekStartDate: LocalDate = LocalDate.now().minusDays(6),

    /**
     * Statistics for the currently selected day
     */
    val dailyStats: DailyStats? = null,

    /**
     * List of statistics for each day in the current week
     */
    val weeklyStats: List<DailyStats> = emptyList(),




    /**
     * Total number of completed Pomodoros (all time)
     */
    val totalCompletedPomodoros: Int = 0,

    /**
     * Total focus minutes across all time
     */
    val totalFocusMinutes: Int = 0,

    /**
     * Average daily focus minutes
     */
    val averageDailyFocusMinutes: Int = 0,

    /**
     * Flag indicating whether data is currently being loaded
     */
    val isLoading: Boolean = false,

    /**
     * Error message to display if data loading fails
     */
    val errorMessage: String? = null
) : UiState {
    /**
     * Get weekly focus data for the chart
     */
    val weeklyFocusData: List<Pair<LocalDate, Int>>
        get() = weeklyStats.map { it.date to it.totalFocusMinutes }

    /**
     * Get the maximum focus minutes in a week for chart scaling
     */
    val maxWeeklyFocusMinutes: Int
        get() = weeklyStats.maxOfOrNull { it.totalFocusMinutes }?.coerceAtLeast(1) ?: 1


}



