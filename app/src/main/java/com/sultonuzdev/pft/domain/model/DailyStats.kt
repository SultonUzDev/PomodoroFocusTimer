package com.sultonuzdev.pft.domain.model


import java.time.LocalDate

/**
 * Data class representing daily focus statistics
 */
data class DailyStats(
    val date: LocalDate,
    val completedPomodoros: Int,
    val totalFocusMinutes: Int
)