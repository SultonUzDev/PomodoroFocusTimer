package com.sultonuzdev.pft.features.stats.data.converter

import androidx.room.TypeConverter
import com.sultonuzdev.pft.core.util.TimerType

/**
 * Type converter for Room to handle TimerType enum
 */
class TimerTypeConverter {
    @TypeConverter
    fun fromString(value: String?): TimerType? {
        return value?.let { enumValueOf<TimerType>(it) }
    }

    @TypeConverter
    fun toString(timerType: TimerType?): String? {
        return timerType?.name
    }
}