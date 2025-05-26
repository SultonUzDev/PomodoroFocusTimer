package com.sultonuzdev.pft.features.timer.data.repository

import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.features.stats.data.repository.PomodoroRepository
import com.sultonuzdev.pft.features.timer.domain.model.AllTimeStats
import com.sultonuzdev.pft.features.timer.domain.model.TodayStats
import com.sultonuzdev.pft.features.timer.domain.repository.StatsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

/**
 * Implementation using your existing SessionRepository
 */

class StatsRepositoryImpl @Inject constructor(
    private val pomodoroRepository: PomodoroRepository
) : StatsRepository {

    override suspend fun getTodayStats(): TodayStats {
        val today = LocalDate.now()

        // Use your existing getDailyStats method
        val dailyStats = pomodoroRepository.getDailyStats(today)

        // Get today's sessions to calculate completed sessions
        val todaySessions = pomodoroRepository.getPomodoroByDate(today)

        // ✅ FIXED: Proper Flow handling
        return combine(dailyStats, todaySessions) { stats, sessions ->
            // Calculate completed sessions (count of long breaks completed)
            val completedSessions = sessions.count {
                it.type == TimerType.LONG_BREAK && it.completed
            }

            TodayStats(
                completedPomodoros = stats.completedPomodoros,
                completedSessions = completedSessions,
                focusTimeMinutes = stats.totalFocusMinutes,
                date = today
            )
        }.first() // ✅ FIXED: Call first() directly on the Flow
    }

    override fun getTodayStatsFlow(): Flow<TodayStats> {
        val today = LocalDate.now()

        // Use your existing methods
        val dailyStats = pomodoroRepository.getDailyStats(today)
        val todaySessions = pomodoroRepository.getPomodoroByDate(today)

        return combine(dailyStats, todaySessions) { stats, sessions ->
            // Calculate completed sessions (count of long breaks completed)
            val completedSessions = sessions.count {
                it.type == TimerType.LONG_BREAK && it.completed
            }

            TodayStats(
                completedPomodoros = stats.completedPomodoros,
                completedSessions = completedSessions,
                focusTimeMinutes = stats.totalFocusMinutes,
                date = today
            )
        }
    }

    override suspend fun getAllTimeStats(): AllTimeStats {
        // ✅ FIXED: Proper Flow handling
        return pomodoroRepository.getAllPomodoros().map { sessions ->
            val totalPomodoros = sessions.count {
                it.type == TimerType.POMODORO && it.completed
            }

            val totalFocusTimeMinutes = sessions
                .filter { it.type == TimerType.POMODORO && it.completed }
                .sumOf { session ->
                    val minutes = java.time.temporal.ChronoUnit.MINUTES.between(
                        session.startTime,
                        session.endTime
                    )
                    minutes.toInt()
                }

            val totalSessions = sessions.count {
                it.type == TimerType.LONG_BREAK && it.completed
            }

            AllTimeStats(
                totalPomodoros = totalPomodoros,
                totalSessions = totalSessions,
                totalFocusTimeMinutes = totalFocusTimeMinutes
            )
        }.first() // ✅ FIXED: Call first() directly on the Flow
    }

    override fun getAllTimeStatsFlow(): Flow<AllTimeStats> {
        return pomodoroRepository.getAllPomodoros().map { sessions ->
            val totalPomodoros = sessions.count {
                it.type == TimerType.POMODORO && it.completed
            }

            val totalFocusTimeMinutes = sessions
                .filter { it.type == TimerType.POMODORO && it.completed }
                .sumOf { session ->
                    val minutes = java.time.temporal.ChronoUnit.MINUTES.between(
                        session.startTime,
                        session.endTime
                    )
                    minutes.toInt()
                }

            val totalSessions = sessions.count {
                it.type == TimerType.LONG_BREAK && it.completed
            }

            AllTimeStats(
                totalPomodoros = totalPomodoros,
                totalSessions = totalSessions,
                totalFocusTimeMinutes = totalFocusTimeMinutes
            )
        }
    }
}