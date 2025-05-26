package com.sultonuzdev.pft.features.timer.domain.model

data class AllTimeStats(
    val totalPomodoros: Int = 0,
    val totalSessions: Int = 0,
    val totalFocusTimeMinutes: Int = 0,
    val longestStreakDays: Int = 0
)