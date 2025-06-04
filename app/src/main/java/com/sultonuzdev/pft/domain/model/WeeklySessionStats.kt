package com.sultonuzdev.pft.domain.model

// Add these data classes to your domain/model package
data class WeeklySessionStats(
    val totalSessions: Int,
    val activeDays: Int,
    val averageSessionsPerDay: Double
)