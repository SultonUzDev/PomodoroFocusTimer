package com.sultonuzdev.pft.presentation.service
object TimerServiceConstants {
    // Service action constants
    const val ACTION_START = "com.sultonuzdev.pft.ACTION_START" // For starting the timer
    const val ACTION_PAUSE = "com.sultonuzdev.pft.ACTION_PAUSE" // For pausing the timer
    const val ACTION_RESUME = "com.sultonuzdev.pft.ACTION_RESUME" // For resuming the timer

    const val ACTION_FINISH = "com.sultonuzdev.pft.ACTION_FINISH" // For finishing the timer even if not completed
    const val ACTION_SKIP = "com.sultonuzdev.pft.ACTION_SKIP" // For skipping the current timer type (no need saving it to database)

    // Notification constants
    const val NOTIFICATION_CHANNEL_ID = "pomodoro_timer_channel"
    const val NOTIFICATION_CHANNEL_NAME = "Pomodoro Timer"
    const val NOTIFICATION_ID = 1001

    // Intent extra keys
    const val EXTRA_TIMER_TYPE = "TIMER_TYPE"
    const val EXTRA_TIMER_DURATION = "DURATION"
    const val EXTRA_TIMER_ACTION = "TIMER_ACTION"
}
