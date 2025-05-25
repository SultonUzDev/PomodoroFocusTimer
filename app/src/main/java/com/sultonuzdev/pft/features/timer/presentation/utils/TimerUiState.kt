package com.sultonuzdev.pft.features.timer.presentation.utils

import com.sultonuzdev.pft.core.ui.utils.UiState
import com.sultonuzdev.pft.core.util.TimerState
import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.features.timer.domain.model.TimerSettings

/**
 * Represents the UI state for the Timer screen
 */
data class TimerUiState(
    val currentType: TimerType = TimerType.POMODORO,
    val timerState: TimerState = TimerState.IDLE,
    val totalTimeMillis: Long = 0L,
    val remainingTimeMillis: Long = 0L,
    val completedPomodoros: Int = 0,
    val completedSessions: Int = 0,
    val formattedTime: String = "00:00",
    val progressFraction: Float = 1.0f,
    val settings: TimerSettings = TimerSettings()
) : UiState