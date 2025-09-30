package com.sultonuzdev.pft.domain.usecase.stats

import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.domain.model.DailyStats
import com.sultonuzdev.pft.domain.repository.PomodoroRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class GetDailyStats @Inject constructor(
    private val repository: PomodoroRepository
) {
    operator fun invoke(date: LocalDate): Flow<DailyStats> {
        return repository.getPomodoroByDate(date).map { sessions ->
            val completedPomodoros = sessions.count {
                it.timerType == TimerType.POMODORO && it.isCompleted
            }

            val totalFocusMinutes = sessions
                .filter { it.timerType == TimerType.POMODORO }
                .sumOf { it.focusedDurationSeconds / 60 }
            DailyStats(
                date = date,
                completedPomodoros = completedPomodoros,
                totalFocusMinutes = totalFocusMinutes.toInt()
            )
        }
    }
}