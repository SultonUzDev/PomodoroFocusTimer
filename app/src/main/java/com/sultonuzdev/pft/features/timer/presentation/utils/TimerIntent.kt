// com.sultonuzdev.pft.features.timer.presentation.utils.TimerIntent.kt
package com.sultonuzdev.pft.features.timer.presentation.utils

import com.sultonuzdev.pft.core.ui.utils.UiIntent
import com.sultonuzdev.pft.core.util.TimerType

/**
 * Represents user actions for the Timer screen
 */
sealed class TimerIntent : UiIntent {
    data object StartTimer : TimerIntent()
    data object PauseTimer : TimerIntent()
    data object ResumeTimer : TimerIntent()
    data object StopTimer : TimerIntent()
    data object SkipTimer : TimerIntent()
    data class ChangeTimerType(val type: TimerType) : TimerIntent()
}

