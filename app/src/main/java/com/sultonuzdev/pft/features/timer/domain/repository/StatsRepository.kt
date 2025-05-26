package com.sultonuzdev.pft.features.timer.domain.repository

import com.sultonuzdev.pft.features.timer.domain.model.AllTimeStats
import com.sultonuzdev.pft.features.timer.domain.model.TodayStats
import kotlinx.coroutines.flow.Flow

interface StatsRepository {
    suspend fun getTodayStats(): TodayStats
    fun getTodayStatsFlow(): Flow<TodayStats>
    suspend fun getAllTimeStats(): AllTimeStats
    fun getAllTimeStatsFlow(): Flow<AllTimeStats>
}