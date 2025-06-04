package com.sultonuzdev.pft.presentation.stats.utils

import com.sultonuzdev.pft.core.ui.utils.UiEffect

/**
 * Represents one-time events for the Stats screen
 */
sealed class StatsEffect : UiEffect {
    /**
     * Show a message to the user
     */
    data class ShowMessage(val message: String) : StatsEffect()



    /**
     * Share statistics with external apps
     */
    data class ShareStats(val statsText: String) : StatsEffect()
}