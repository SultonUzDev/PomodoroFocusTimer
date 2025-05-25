// com.sultonuzdev.pft.features.stats.data.repository.SessionRepositoryImpl.kt
package com.sultonuzdev.pft.features.stats.data.repository

import android.util.Log
import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.features.stats.data.datasource.SessionDao
import com.sultonuzdev.pft.features.stats.data.entity.toDomainModel
import com.sultonuzdev.pft.features.stats.data.entity.toEntity
import com.sultonuzdev.pft.features.stats.domain.model.DailyStats
import com.sultonuzdev.pft.features.stats.domain.model.TimerSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

/**
 * Implementation of SessionRepository that uses Room database for persistence
 */
class SessionRepositoryImpl @Inject constructor(
    private val sessionDao: SessionDao
) : SessionRepository {

    override suspend fun saveSession(session: TimerSession) {
        val entity = session.toEntity()
        sessionDao.insertSession(entity)
    }

    override fun getAllSessions(): Flow<List<TimerSession>> {
        return sessionDao.getAllSessions().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getSessionsByDate(date: LocalDate): Flow<List<TimerSession>> {
        // Convert LocalDate to LocalDateTime at start of day
        val startOfDay = date.atStartOfDay()
        val endOfDay = date.atTime(LocalTime.MAX)

        return sessionDao.getSessionsByDateRange(startOfDay, endOfDay).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getDailyStats(date: LocalDate): Flow<DailyStats> {
        return getSessionsByDate(date).map { sessions ->
            val completedPomodoros = sessions.count {
                it.type == TimerType.POMODORO && it.completed
            }

            val totalFocusMinutes = sessions
                .filter { it.type == TimerType.POMODORO }

                .sumOf {
                    val minutes = ChronoUnit.MINUTES.between(it.startTime, it.endTime)
                    Log.d("mlog", "Daily totalFocusMinutes: $minutes ")
                    minutes.toInt()
                }

            DailyStats(
                date = date,
                completedPomodoros = completedPomodoros,
                totalFocusMinutes = totalFocusMinutes
            )
        }
    }

    override fun getWeeklyStats(startDate: LocalDate): Flow<List<DailyStats>> {
        // Generate the 7 days of the week
        val dates = (0..6).map { startDate.plusDays(it.toLong()) }

        return getAllSessions().map { allSessions ->
            dates.map { date ->
                // Filter sessions for this date
                val sessionsForDay = allSessions.filter { session ->
                    val sessionDate = session.startTime.toLocalDate()
                    sessionDate == date
                }

                // Calculate stats
                val completedPomodoros = sessionsForDay.count {
                    it.type == TimerType.POMODORO && it.completed
                }

                val totalFocusMinutes = sessionsForDay
                    .filter { it.type == TimerType.POMODORO }
                    .sumOf {
                        val minutes = ChronoUnit.MINUTES.between(it.startTime, it.endTime)
                        Log.d("mlog", "Weekly totalFocusMinutes: $minutes ")
                        minutes.toInt()
                    }

                DailyStats(
                    date = date,
                    completedPomodoros = completedPomodoros,
                    totalFocusMinutes = totalFocusMinutes
                )
            }
        }
    }

}