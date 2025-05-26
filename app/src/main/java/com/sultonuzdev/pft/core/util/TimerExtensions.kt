package com.sultonuzdev.pft.core.util


import android.annotation.SuppressLint
import com.sultonuzdev.pft.core.util.Constants.MILLIS_IN_MINUTE
import com.sultonuzdev.pft.core.util.Constants.MILLIS_IN_SECOND
import com.sultonuzdev.pft.core.util.Constants.SECONDS_IN_MINUTE
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Extension functions for timer-related conversions
 */

/**
 * Converts minutes to milliseconds
 */
fun Int.minutesToMillis(): Long = this * MILLIS_IN_MINUTE

/**
 * Formats milliseconds to a MM:SS string representation
 */
@SuppressLint("DefaultLocale")
fun Long.formatToMinutesAndSeconds(): String {
    val totalSeconds = this / MILLIS_IN_SECOND
    val minutes = totalSeconds / SECONDS_IN_MINUTE
    val seconds = totalSeconds % SECONDS_IN_MINUTE
    return String.format("%02d:%02d", minutes, seconds)
}

/**
 * Calculates the progress fraction (0.0 to 1.0) for a timer
 */
fun calculateProgress(elapsedTimeMillis: Long, totalTimeMillis: Long): Float {
    return (elapsedTimeMillis / totalTimeMillis.toFloat()).coerceIn(0f, 1f)
}

fun LocalDateTime.formatToDate(): String {
    return this.format(DateTimeFormatter.ISO_DATE)
}

// Helper function for formatting focus time
 fun Int.formatFocusTime(): String {
    return when {
        this < 60 -> "${this}m"
        this < 1440 -> { // Less than 24 hours
            val hours = this / 60
            val minutes = this % 60
            if (minutes == 0) "${hours}h" else "${hours}h ${minutes}m"
        }
        else -> { // 24+ hours
            val days = this / 1440
            val hours = (this % 1440) / 60
            if (hours == 0) "${days}d" else "${days}d ${hours}h"
        }
    }
}