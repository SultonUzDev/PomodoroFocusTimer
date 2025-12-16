package com.sultonuzdev.pft.domain.model

import com.sultonuzdev.pft.core.util.Constants.DEFAULT_POMODORO_MINUTES
import com.sultonuzdev.pft.core.util.TimerState
import com.sultonuzdev.pft.core.util.TimerType
import java.time.LocalDate

data class NewTimerState(
    // Timer state
    val settings: PomodoroTimerSettings = PomodoroTimerSettings(),


    val currentType: TimerType = TimerType.POMODORO,
    val timerState: TimerState = TimerState.IDLE,
    val totalTimeMillis: Long = DEFAULT_POMODORO_MINUTES * 60 * 1000L,
    val remainingTimeMillis: Long = DEFAULT_POMODORO_MINUTES * 60 * 1000L,
    val formattedTime: String = "25:00",
    val currentTimeMillis: Long = 0L,
    val startedAt: LocalDate = LocalDate.now(),


    // Current session tracking
    val currentSessionPomodoros: Int = 0,
)
