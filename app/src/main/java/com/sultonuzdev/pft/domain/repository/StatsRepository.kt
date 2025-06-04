package com.sultonuzdev.pft.domain.repository


import com.sultonuzdev.pft.domain.model.TodayStats
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface StatsRepository {

    fun getTodayStatsFlow(): Flow<TodayStats>





}

