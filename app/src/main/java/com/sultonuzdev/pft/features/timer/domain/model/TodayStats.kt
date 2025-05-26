package com.sultonuzdev.pft.features.timer.domain.model

import java.time.LocalDate

data class TodayStats(
    val completedPomodoros: Int = 0,
    val completedSessions: Int = 0,
    val focusTimeMinutes: Int = 0,
    val date: LocalDate = LocalDate.now()
)