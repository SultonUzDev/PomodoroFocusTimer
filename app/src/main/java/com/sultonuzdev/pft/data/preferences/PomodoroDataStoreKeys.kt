package com.sultonuzdev.pft.data.preferences

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PomodoroDataStoreKeys {
    val POMODORO_MINUTES = intPreferencesKey("pomodoro_minutes")
    val SHORT_BREAK_MINUTES = intPreferencesKey("short_break_minutes")
    val LONG_BREAK_MINUTES = intPreferencesKey("long_break_minutes")
    val POMODOROS_BEFORE_LONG_BREAK = intPreferencesKey("pomodoros_before_long_break")
    val VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
    val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")

    val THEME_MODE_KEY = stringPreferencesKey("theme_mode")

    val TIMER_STYLE_KEY = stringPreferencesKey("timer_style")
}