package com.sultonuzdev.pft.domain.usecase.pomodoro

import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.domain.model.Pomodoro
import com.sultonuzdev.pft.domain.repository.PomodoroRepository
import java.time.LocalDate
import javax.inject.Inject

/**
 * Use case for saving a completed timer session
 */
class AddPomodoro @Inject constructor(
    private val repository: PomodoroRepository
) {
    suspend operator fun invoke(
        type: TimerType,
        durationMinutes: Int,
        completed: Boolean,
        startedTime: LocalDate,
        focusedDurationSeconds: Long,
    ) {
        val session = Pomodoro(
            id = 0,
            timerType = type,
            plannedDurationSeconds = durationMinutes.toLong(),
            isCompleted = completed,
            startedAt = startedTime,
            focusedDurationSeconds = focusedDurationSeconds
        )
        repository.savePomodoro(session)
    }
}