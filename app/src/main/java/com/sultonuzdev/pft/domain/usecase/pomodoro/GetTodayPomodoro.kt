package com.sultonuzdev.pft.domain.usecase.pomodoro

import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.domain.model.DailyStats
import com.sultonuzdev.pft.domain.repository.PomodoroRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class GetTodayPomodoro @Inject constructor(
    private val repository: PomodoroRepository
) {
    operator fun invoke(): Flow<DailyStats> {

        val today = LocalDate.now()

        val todaySessions = repository.getPomodoroByDate(today)
        return todaySessions.map { sessions ->
            val completedPomodoros = sessions.count {
                it.timerType == TimerType.POMODORO && it.isCompleted
            }

            val totalFocusMinutes = sessions
                .filter { it.timerType == TimerType.POMODORO }
                .sumOf { it.focusedDurationSeconds }
            DailyStats(
                date = today,
                completedPomodoros = completedPomodoros,
                totalFocusMinutes = totalFocusMinutes.toInt()
            )
        }


    }
}