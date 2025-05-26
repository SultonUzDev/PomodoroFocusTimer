package com.sultonuzdev.pft.features.stats.domain.model

import com.sultonuzdev.pft.core.util.TimerType
import java.time.LocalDateTime

/**
 * Data class representing a completed timer session
 */
data class Pomodoro(
    val id: Long = 0,
    val type: TimerType,
    val durationMinutes: Int,
    val completed: Boolean,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime
)