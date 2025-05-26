package com.sultonuzdev.pft.features.timer.presentation.utils

import com.sultonuzdev.pft.core.util.TimerState
import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.features.timer.domain.model.AllTimeStats
import com.sultonuzdev.pft.features.timer.domain.model.TimerSettings
import com.sultonuzdev.pft.features.timer.domain.model.TodayStats

/**
 * UI state for the Timer screen with enhanced statistics
 */
data class TimerUiState(
    // Timer state
    val settings: TimerSettings = TimerSettings(),
    val currentType: TimerType = TimerType.POMODORO,
    val timerState: TimerState = TimerState.IDLE,
    val totalTimeMillis: Long = 25 * 60 * 1000L,
    val remainingTimeMillis: Long = 25 * 60 * 1000L,
    val formattedTime: String = "25:00",
    val progressFraction: Float = 1.0f,
    val currentTimeMillis: Long = 0L,

    // Current session tracking
    val currentSessionPomodoros: Int = 0,

    // Today's statistics
    val todayStats: TodayStats = TodayStats(),

    // All-time statistics
    val allTimeStats: AllTimeStats = AllTimeStats(),

    // Loading state
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)