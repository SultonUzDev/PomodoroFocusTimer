package com.sultonuzdev.pft.data.mapper

import com.sultonuzdev.pft.data.db.entity.PomodoroEntity
import com.sultonuzdev.pft.domain.model.Pomodoro
import java.time.LocalDate

/**
 * Extension function to convert from entity to domain model
 */
fun PomodoroEntity.toDomainModel(): Pomodoro {
    return Pomodoro(
        id = id,
        timerType = timerType,
        plannedDurationSeconds = plannedDurationSeconds,
        focusedDurationSeconds = focusedDurationSeconds,
        isCompleted = isCompleted,
        startedAt = LocalDate.parse(startedAt),
    )
}

/**
 * Extension function to convert from domain model to entity
 */
fun Pomodoro.toEntity(): PomodoroEntity {
    return PomodoroEntity(
        id = id,
        timerType = timerType,
        plannedDurationSeconds = plannedDurationSeconds,
        focusedDurationSeconds = focusedDurationSeconds,
        isCompleted = isCompleted,
        startedAt = startedAt.toString(),
    )
}

fun List<PomodoroEntity>.toDomainModel(): List<Pomodoro> {
    return map { it.toDomainModel() }
}
