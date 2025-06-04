// com.sultonuzdev.pft.features.stats.data.entity.SessionEntity.kt
package com.sultonuzdev.pft.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.data.db.converter.DateTimeConverter
import com.sultonuzdev.pft.data.db.converter.TimerTypeConverter
import com.sultonuzdev.pft.domain.model.Pomodoro
import java.time.LocalDateTime

/**
 * Room entity for timer sessions
 */
@Entity(tableName = "pomodoros")
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

