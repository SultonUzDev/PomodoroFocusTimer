package com.sultonuzdev.pft.data.repository

import android.util.Log
import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.data.db.datasource.PomodoroDao
import com.sultonuzdev.pft.data.mapper.toDomainModel
import com.sultonuzdev.pft.data.mapper.toEntity
import com.sultonuzdev.pft.domain.repository.PomodoroRepository
import com.sultonuzdev.pft.domain.model.DailyStats
import com.sultonuzdev.pft.domain.model.Pomodoro
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

/**
 * Implementation of SessionRepository that uses Room database for persistence
 */
class PomodoroRepositoryImpl @Inject constructor(
    private val pomodoroDao: PomodoroDao
) : PomodoroRepository {

    override suspend fun savePomodoro(pomodoro: Pomodoro) {
        val entity = pomodoro.toEntity()
        pomodoroDao.insertSession(entity)
    }

    override fun getAllPomodoros(): Flow<List<Pomodoro>> {
        return pomodoroDao.getAllSessions().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getPomodoroByDate(date: LocalDate): Flow<List<Pomodoro>> {
        // Convert LocalDate to LocalDateTime at start of day
        val startOfDay = date.atStartOfDay()
        val endOfDay = date.atTime(LocalTime.MAX)

        return pomodoroDao.getSessionsByDateRange(startOfDay, endOfDay).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getDailyStats(date: LocalDate): Flow<DailyStats> {
        return getPomodoroByDate(date).map { sessions ->
            val completedPomodoros = sessions.count {
                it.type == TimerType.POMODORO && it.completed
            }

            val totalFocusMinutes = sessions
                .filter { it.type == TimerType.POMODORO }

                .sumOf {
                    val minutes = ChronoUnit.MINUTES.between(it.startTime, it.endTime)
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

        return getAllPomodoros().map { allSessions ->
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