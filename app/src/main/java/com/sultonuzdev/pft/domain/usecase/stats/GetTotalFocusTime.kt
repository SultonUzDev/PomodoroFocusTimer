package com.sultonuzdev.pft.domain.usecase.stats

import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.domain.repository.PomodoroRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTotalFocusTime @Inject constructor(
    private val repository: PomodoroRepository
) {
    operator fun invoke(): Flow<Int> {
        return repository.getAllPomodoros().map { sessions ->
            sessions.filter { it.timerType == TimerType.POMODORO }
                .sumOf { it.focusedDurationSeconds / 60 }.toInt()
        }
    }
}