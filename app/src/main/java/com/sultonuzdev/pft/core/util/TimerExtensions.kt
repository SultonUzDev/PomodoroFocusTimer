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