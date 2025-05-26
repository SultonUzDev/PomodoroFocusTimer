// com.sultonuzdev.pft.features.stats.data.entity.SessionEntity.kt
package com.sultonuzdev.pft.features.stats.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.features.stats.data.converter.DateTimeConverter
import com.sultonuzdev.pft.features.stats.data.converter.TimerTypeConverter
import com.sultonuzdev.pft.features.stats.domain.model.Pomodoro
import java.time.LocalDateTime

/**
 * Room entity for timer sessions
 */
@Entity(tableName = "sessions")
@TypeConverters(DateTimeConverter::class, TimerTypeConverter::class)
data class PomodoroEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: TimerType,
    val durationMinutes: Int,
    val completed: Boolean,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime
)

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