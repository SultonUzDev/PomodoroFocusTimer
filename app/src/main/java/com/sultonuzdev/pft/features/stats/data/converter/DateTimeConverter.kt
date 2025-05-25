package com.sultonuzdev.pft.features.stats.data.converter

import android.annotation.SuppressLint
import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Type converter for Room to handle LocalDateTime objects
 */
class DateTimeConverter {
    @SuppressLint("NewApi")
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @SuppressLint("NewApi")
    @TypeConverter
    fun fromString(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, formatter) }
    }

    @SuppressLint("NewApi")
    @TypeConverter
    fun toString(dateTime: LocalDateTime?): String? {
        return dateTime?.format(formatter)
    }
}