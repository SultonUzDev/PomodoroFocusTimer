package com.sultonuzdev.pft.data.mapper

import com.sultonuzdev.pft.data.db.entity.PomodoroEntity
import com.sultonuzdev.pft.domain.model.DailySession
import com.sultonuzdev.pft.domain.model.Pomodoro

/**
 * Extension function to convert from entity to domain model
 */
fun PomodoroEntity.toDomainModel(): Pomodoro         {
    return Pomodoro(
        id = id,
        type = type,
        durationMinutes = durationMinutes,
        completed = completed,
        startTime = startTime,
        endTime = endTime
    )
}

/**
 * Extension function to convert from domain model to entity
 */
fun Pomodoro.toEntity(): PomodoroEntity {
    return PomodoroEntity(
        id = id,
        type = type,
        durationMinutes = durationMinutes,
        completed = completed,
        startTime = startTime,
        endTime = endTime
    )
}
