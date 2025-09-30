package com.sultonuzdev.pft.domain.model

import com.sultonuzdev.pft.core.util.TimerType
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Data class representing a completed timer session
 */
data class Pomodoro(
    val id: Long,
    val timerType: TimerType,
    val plannedDurationSeconds: Long,
    val focusedDurationSeconds: Long,
    val isCompleted: Boolean,
    val startedAt: LocalDate,
)