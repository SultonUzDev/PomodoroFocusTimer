package com.sultonuzdev.pft.data.db.converter

import android.annotation.SuppressLint
import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Type converter for Room to handle LocalDateTime objects
 */
class DateTimeConverter {
    @SuppressLint("NewApi")
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @SuppressLint("NewApi")
    @TypeConverter
    fun fromString(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it, formatter) }
    }

    @SuppressLint("NewApi")
    @TypeConverter
    fun toString(dateTime: LocalDate?): String? {
        return dateTime?.format(formatter)
    }
}