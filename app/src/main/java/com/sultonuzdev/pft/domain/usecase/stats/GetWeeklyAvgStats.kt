package com.sultonuzdev.pft.domain.usecase.stats

import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.domain.model.DailyStats
import com.sultonuzdev.pft.domain.repository.PomodoroRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

class GetWeeklyAvgStats @Inject constructor(
    private val repository: PomodoroRepository,
) {

    operator fun invoke(date: LocalDate): Flow<List<DailyStats>> {
        val startOfWeek = getStartOfWeek(date)
        val endOfWeek = startOfWeek.plusDays(6)

        return repository.getWeeklyStats(startOfWeek, endOfWeek).map { weekSessions ->
            // Group sessions by date
            val sessionsByDate = weekSessions.groupBy { it.startedAt }
            
            // Generate all 7 days of the week
            val weekDays = (0..6).map { startOfWeek.plusDays(it.toLong()) }
            
            // Calculate daily stats for each day of the week
            weekDays.map { day ->
                val sessionsForDay = sessionsByDate[day] ?: emptyList()
                
                val completedPomodoros = sessionsForDay.count {
                    it.timerType == TimerType.POMODORO && it.isCompleted
                }
                
                val totalFocusSeconds = sessionsForDay
                    .filter { it.timerType == TimerType.POMODORO }
                    .sumOf { it.focusedDurationSeconds }
                
                DailyStats(
                    date = day,
                    completedPomodoros = completedPomodoros,
                    totalFocusMinutes = (totalFocusSeconds / 60).toInt()
                )
            }
        }
    }

    /**
     * Get the start date of a week containing the provided date
     * Uses Monday as the first day of the week for consistency
     */
    private fun getStartOfWeek(date: LocalDate): LocalDate {
        return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    }
}