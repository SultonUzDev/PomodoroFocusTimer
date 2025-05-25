package com.sultonuzdev.pft.features.settings.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.sultonuzdev.pft.core.util.Constants.DEFAULT_LONG_BREAK_MINUTES
import com.sultonuzdev.pft.core.util.Constants.DEFAULT_POMODORO_MINUTES
import com.sultonuzdev.pft.core.util.Constants.DEFAULT_POMODOROS_BEFORE_LONG_BREAK
import com.sultonuzdev.pft.core.util.Constants.DEFAULT_SHORT_BREAK_MINUTES
import com.sultonuzdev.pft.features.settings.domain.repository.TimerSettingsRepository
import com.sultonuzdev.pft.features.timer.domain.model.TimerSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of TimerSettingsRepository that uses DataStore for persistence
 */
class TimerSettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : TimerSettingsRepository {

    companion object {
        private val POMODORO_MINUTES = intPreferencesKey("pomodoro_minutes")
        private val SHORT_BREAK_MINUTES = intPreferencesKey("short_break_minutes")
        private val LONG_BREAK_MINUTES = intPreferencesKey("long_break_minutes")
        private val POMODOROS_BEFORE_LONG_BREAK = intPreferencesKey("pomodoros_before_long_break")
        private val VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
        private val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        private val FOCUS_MODE_ENABLED = booleanPreferencesKey("focus_mode_enabled")
    }

    override fun getSettings(): Flow<TimerSettings> {
        return dataStore.data.map { preferences ->
            TimerSettings(
                pomodoroMinutes = preferences[POMODORO_MINUTES] ?: DEFAULT_POMODORO_MINUTES,
                shortBreakMinutes = preferences[SHORT_BREAK_MINUTES] ?: DEFAULT_SHORT_BREAK_MINUTES,
                longBreakMinutes = preferences[LONG_BREAK_MINUTES] ?: DEFAULT_LONG_BREAK_MINUTES,
                pomodorosBeforeLongBreak = preferences[POMODOROS_BEFORE_LONG_BREAK]
                    ?: DEFAULT_POMODOROS_BEFORE_LONG_BREAK,
                vibrationEnabled = preferences[VIBRATION_ENABLED] != false,
                soundEnabled = preferences[SOUND_ENABLED] != false,
                enableFocusMode = preferences[FOCUS_MODE_ENABLED] == true
            )
        }
    }

    override suspend fun updateSettings(settings: TimerSettings) {
        dataStore.edit { preferences ->
            preferences[POMODORO_MINUTES] = settings.pomodoroMinutes
            preferences[SHORT_BREAK_MINUTES] = settings.shortBreakMinutes
            preferences[LONG_BREAK_MINUTES] = settings.longBreakMinutes
            preferences[POMODOROS_BEFORE_LONG_BREAK] = settings.pomodorosBeforeLongBreak
            preferences[VIBRATION_ENABLED] = settings.vibrationEnabled
            preferences[SOUND_ENABLED] = settings.soundEnabled
            preferences[FOCUS_MODE_ENABLED] = settings.enableFocusMode
        }
    }

}