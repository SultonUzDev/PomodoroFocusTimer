package com.sultonuzdev.pft.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.data.db.converter.TimerTypeConverter

/**
 * Room entity for timer sessions
 */
@Entity(tableName = "pomodoros")
@TypeConverters(TimerTypeConverter::class)
data class PomodoroEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timerType: TimerType,             // FOCUS, SHORT_BREAK, LONG_BREAK
    val plannedDurationSeconds: Long,      // scheduled session length in seconds
    val focusedDurationSeconds: Long,      // how much time was actually focused

    val isCompleted: Boolean,             // true if session ran fully
    val startedAt: String,           // session start timestamp
)

