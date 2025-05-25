package com.sultonuzdev.pft.features.stats.data.repository

import com.sultonuzdev.pft.features.stats.domain.model.DailyStats
import com.sultonuzdev.pft.features.stats.domain.model.TimerSession
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Repository interface for saving and retrieving timer sessions
 */
interface SessionRepository {
    suspend fun saveSession(session: TimerSession)
    fun getAllSessions(): Flow<List<TimerSession>>
    fun getSessionsByDate(date: LocalDate): Flow<List<TimerSession>>
    fun getDailyStats(date: LocalDate): Flow<DailyStats>
    fun getWeeklyStats(startDate: LocalDate): Flow<List<DailyStats>>

}