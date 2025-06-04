package com.sultonuzdev.pft.presentation.service
object TimerServiceConstants {
    // Service action constants
    const val ACTION_START = "com.sultonuzdev.pft.ACTION_START"
    const val ACTION_PAUSE = "com.sultonuzdev.pft.ACTION_PAUSE"
    const val ACTION_RESUME = "com.sultonuzdev.pft.ACTION_RESUME"
    const val ACTION_STOP = "com.sultonuzdev.pft.ACTION_STOP"
    const val ACTION_SKIP = "com.sultonuzdev.pft.ACTION_SKIP"

    // Notification constants
    const val NOTIFICATION_CHANNEL_ID = "pomodoro_timer_channel"
    const val NOTIFICATION_CHANNEL_NAME = "Pomodoro Timer"
    const val NOTIFICATION_ID = 1001

    // Intent extra keys
    const val EXTRA_TIMER_TYPE = "TIMER_TYPE"
    const val EXTRA_TIMER_DURATION = "DURATION"
}
