package com.sultonuzdev.pft.data.repository

import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.domain.model.DailyStats
import com.sultonuzdev.pft.domain.model.TodayStats
import com.sultonuzdev.pft.domain.repository.PomodoroRepository
import com.sultonuzdev.pft.domain.repository.StatsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class StatsRepositoryImpl @Inject constructor(
    private val pomodoroRepository: PomodoroRepository,
) : StatsRepository {

    override fun getTodayStatsFlow(): Flow<TodayStats> {
        val today = LocalDate.now()

        val dailyStats = pomodoroRepository.getDailyStats(today)
        val todaySessions = pomodoroRepository.getPomodoroByDate(today)

        return combine(dailyStats, todaySessions) { stats, _ ->
            TodayStats(
                completedPomodoros = stats.completedPomodoros,
                focusTimeMinutes = stats.totalFocusMinutes,
                date = today
            )
        }
    }

    override fun getWeeklyStats(startDate: LocalDate): Flow<List<DailyStats>> {
        return pomodoroRepository.getWeeklyStats(startDate)
    }

    override fun getTotalCompletedPomodoros(): Flow<Int> {
        return pomodoroRepository.getAllPomodoros().map { sessions ->
            sessions.count { it.type == TimerType.POMODORO && it.completed }
        }
    }

    override fun getTotalFocusMinutes(): Flow<Int> {
        return pomodoroRepository.getAllPomodoros().map { sessions ->
            sessions.filter { it.type == TimerType.POMODORO }.sumOf {
                ChronoUnit.MINUTES.between(it.startTime, it.endTime).toInt()
            }
        }
    }
}