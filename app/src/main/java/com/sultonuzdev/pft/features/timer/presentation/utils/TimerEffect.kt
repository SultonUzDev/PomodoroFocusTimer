package com.sultonuzdev.pft.features.timer.presentation.utils

import com.sultonuzdev.pft.core.ui.utils.UiEffect

/**
 * Represents one-time events for the Timer screen
 */
sealed class TimerEffect : UiEffect {
    data class ShowMessage(val message: String) : TimerEffect()
    data object PlayTimerCompletedSound : TimerEffect()
    data object VibrateDevice : TimerEffect()
    data class ShowQuote(val quote: String) : TimerEffect()
}