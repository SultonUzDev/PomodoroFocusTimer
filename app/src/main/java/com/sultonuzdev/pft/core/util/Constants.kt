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

    // Database
    const val DATABASE_NAME = "pomodoro_database"

    // Timer delays and intervals
    const val TIMER_COMPLETION_DELAY_MILLIS = 2000L  // Delay before auto-transition (2 seconds)
    const val PAUSE_LOOP_DELAY_MILLIS = 100L         // Delay when paused to prevent tight loop
    const val NOTIFICATION_UPDATE_INTERVAL_MILLIS = 5000L  // Update notification every 5 seconds

    // UI scaling factors
    const val TIMER_CIRCLE_SIZE_PHONE = 0.8f        // 70% of screen width for phone
    const val TIMER_CIRCLE_SIZE_TABLET = 0.7f       // 70% of screen width for tablet
    const val TABLET_CONTENT_WIDTH = 0.9f           // 90% width for tablet content
    const val TABLET_CONTROLS_WIDTH = 0.8f          // 80% width for tablet controls
}