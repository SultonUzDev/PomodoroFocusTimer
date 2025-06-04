package com.sultonuzdev.pft.data.repository

import com.sultonuzdev.pft.domain.model.TodayStats
import com.sultonuzdev.pft.domain.repository.PomodoroRepository
import com.sultonuzdev.pft.domain.repository.StatsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import java.time.LocalDate
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








}