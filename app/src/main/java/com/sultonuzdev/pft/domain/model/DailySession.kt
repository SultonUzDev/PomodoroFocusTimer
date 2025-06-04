package com.sultonuzdev.pft.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

data class DailySession(
    val id: Long = 0,
    val date: LocalDate,
    val completedSessionsCount: Int,
    val lastUpdated: LocalDateTime
)