package com.sultonuzdev.pft.domain.usecase.stats

import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.domain.repository.PomodoroRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class GetCompletedPomodoros(
    private val repository: PomodoroRepository
) {
    operator fun invoke(date: LocalDate): Flow<Int> {
        return repository.getAllPomodoros().map { sessions ->
            sessions.count { it.timerType == TimerType.POMODORO && it.isCompleted }
        }
    }

}