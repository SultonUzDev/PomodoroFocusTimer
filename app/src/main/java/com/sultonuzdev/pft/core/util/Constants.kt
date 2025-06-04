package com.sultonuzdev.pft.core.util

object Constants {
    // Default durations in minutes
    const val DEFAULT_POMODORO_MINUTES: Int = 25
    const val DEFAULT_SHORT_BREAK_MINUTES: Int = 5
    const val DEFAULT_LONG_BREAK_MINUTES: Int = 15

    // After how many pomodoros should a long break occur
    const val DEFAULT_POMODORO_CYCLE_LENGTH: Int = 4

    // Millisecond conversions
    const val MILLIS_IN_SECOND = 1000L
    const val SECONDS_IN_MINUTE = 60L
    const val MILLIS_IN_MINUTE = SECONDS_IN_MINUTE * MILLIS_IN_SECOND
    const val DATABASE_NAME = "pomodoro_database"


}