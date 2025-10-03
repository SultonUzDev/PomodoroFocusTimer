package com.sultonuzdev.pft.domain.usecase.stats

import android.util.Log
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

        Log.d("GetWeeklyAvgStats", "Requesting stats from $startOfWeek to $endOfWeek")

        return repository.getWeeklyStats(startOfWeek, endOfWeek).map { weekSessions ->
            Log.d("GetWeeklyAvgStats", "Received ${weekSessions.size} sessions from repository")

            // Group sessions by date
            val sessionsByDate = weekSessions.groupBy { it.startedAt }
            Log.d("GetWeeklyAvgStats", "Sessions grouped into ${sessionsByDate.size} days")

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

                val totalFocusMinutes = (totalFocusSeconds / 60).toInt()

                // Count completed cycles (each completed LONG_BREAK = 1 cycle)
                val completedCycles = sessionsForDay.count {
                    it.timerType == TimerType.LONG_BREAK && it.isCompleted
                }

                Log.d("GetWeeklyAvgStats", "  $day: $completedPomodoros pomodoros, $totalFocusMinutes minutes, $completedCycles cycles (${sessionsForDay.size} sessions)")

                DailyStats(
                    date = day,
                    completedPomodoros = completedPomodoros,
                    totalFocusMinutes = totalFocusMinutes,
                    completedCycles = completedCycles
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