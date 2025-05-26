package com.sultonuzdev.pft.features.timer.domain.usecase

import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.features.stats.data.repository.PomodoroRepository
import com.sultonuzdev.pft.features.stats.domain.model.Pomodoro
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * Use case for saving a completed timer session
 */
class SaveTimerSessionUseCase @Inject constructor(
    private val repository: PomodoroRepository
) {
    suspend operator fun invoke(
        type: TimerType,
        durationMinutes: Int,
        completed: Boolean,
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ) {
        val session = Pomodoro(
            type = type,
            durationMinutes = durationMinutes,
            completed = completed,
            startTime = startTime,
            endTime = endTime
        )
        repository.savePomodoro(session)
    }
}