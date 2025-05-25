package com.sultonuzdev.pft.features.stats.presentation.stats.utils

import com.sultonuzdev.pft.core.ui.utils.UiIntent
import java.time.LocalDate

/**
 * Represents user actions for the Stats screen
 */
sealed class StatsIntent : UiIntent {
    /**
     * User selects a specific date to view statistics
     */
    data class SelectDate(val date: LocalDate) : StatsIntent()

    /**
     * User requests to load weekly statistics
     */
    data object LoadWeeklyStats : StatsIntent()

    /**
     * User requests to navigate to the previous week
     */
    data object NavigateToPreviousWeek : StatsIntent()


    /**
     * User requests to navigate to the next week
     */
    data object NavigateToNextWeek : StatsIntent()

    /**
     * User requests to refresh all statistics data
     */
    data object RefreshStats : StatsIntent()

}