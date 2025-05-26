package com.sultonuzdev.pft.features.timer.domain.model


import com.sultonuzdev.pft.core.util.Constants.DEFAULT_LONG_BREAK_MINUTES
import com.sultonuzdev.pft.core.util.Constants.DEFAULT_POMODOROS_BEFORE_LONG_BREAK
import com.sultonuzdev.pft.core.util.Constants.DEFAULT_POMODORO_MINUTES
import com.sultonuzdev.pft.core.util.Constants.DEFAULT_SHORT_BREAK_MINUTES

/**
 * Data class representing user's timer settings
 */
data class TimerSettings(
    val pomodoroMinutes: Int = DEFAULT_POMODORO_MINUTES,
    val shortBreakMinutes: Int = DEFAULT_SHORT_BREAK_MINUTES,
    val longBreakMinutes: Int = DEFAULT_LONG_BREAK_MINUTES,
    val pomodorosBeforeLongBreak: Int = DEFAULT_POMODOROS_BEFORE_LONG_BREAK,
    val vibrationEnabled: Boolean = true,
    val soundEnabled: Boolean = true,
    val enableFocusMode: Boolean = false,
)

