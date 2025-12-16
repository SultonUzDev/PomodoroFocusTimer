package com.sultonuzdev.pft.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.sultonuzdev.pft.core.enums.TimerStyle
import com.sultonuzdev.pft.core.ui.theme.ThemeMode
import com.sultonuzdev.pft.core.util.Constants.DEFAULT_LONG_BREAK_MINUTES
import com.sultonuzdev.pft.core.util.Constants.DEFAULT_POMODORO_CYCLE_LENGTH
import com.sultonuzdev.pft.core.util.Constants.DEFAULT_POMODORO_MINUTES
import com.sultonuzdev.pft.core.util.Constants.DEFAULT_SHORT_BREAK_MINUTES
import com.sultonuzdev.pft.domain.model.PomodoroTimerSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesManager(
    private val dataStore: DataStore<Preferences>
) {
    fun getSettings(): Flow<PomodoroTimerSettings> {
        return dataStore.data.map { preferences ->
            PomodoroTimerSettings(
                pomodoroMinutes = preferences[PomodoroDataStoreKeys.POMODORO_MINUTES]
                    ?: DEFAULT_POMODORO_MINUTES,
                shortBreakMinutes = preferences[PomodoroDataStoreKeys.SHORT_BREAK_MINUTES]
                    ?: DEFAULT_SHORT_BREAK_MINUTES,
                longBreakMinutes = preferences[PomodoroDataStoreKeys.LONG_BREAK_MINUTES]
                    ?: DEFAULT_LONG_BREAK_MINUTES,
                pomodoroCycleLength = preferences[PomodoroDataStoreKeys.POMODOROS_BEFORE_LONG_BREAK]
                    ?: DEFAULT_POMODORO_CYCLE_LENGTH,
                vibrationEnabled = preferences[PomodoroDataStoreKeys.VIBRATION_ENABLED] ?: true,
                soundEnabled = preferences[PomodoroDataStoreKeys.SOUND_ENABLED] ?: true,
            )
        }
    }

    suspend fun updateSettings(settings: PomodoroTimerSettings) {
        dataStore.edit { preferences ->
            preferences[PomodoroDataStoreKeys.POMODORO_MINUTES] = settings.pomodoroMinutes
            preferences[PomodoroDataStoreKeys.SHORT_BREAK_MINUTES] = settings.shortBreakMinutes
            preferences[PomodoroDataStoreKeys.LONG_BREAK_MINUTES] = settings.longBreakMinutes
            preferences[PomodoroDataStoreKeys.POMODOROS_BEFORE_LONG_BREAK] =
                settings.pomodoroCycleLength
            preferences[PomodoroDataStoreKeys.VIBRATION_ENABLED] = settings.vibrationEnabled
            preferences[PomodoroDataStoreKeys.SOUND_ENABLED] = settings.soundEnabled
        }
    }

    fun getThemeMode(): Flow<ThemeMode> {
        return dataStore.data.map { preferences ->
            val themeModeString =
                preferences[PomodoroDataStoreKeys.THEME_MODE_KEY] ?: ThemeMode.SYSTEM.name
            try {
                ThemeMode.valueOf(themeModeString)
            } catch (_: IllegalArgumentException) {
                ThemeMode.SYSTEM
            }
        }
    }

    suspend fun setThemeMode(themeMode: ThemeMode) {
        dataStore.edit { preferences ->
            preferences[PomodoroDataStoreKeys.THEME_MODE_KEY] = themeMode.name
        }
    }

    fun getTimerStyle(): Flow<TimerStyle> {
        return dataStore.data.map { preferences ->
            val themeTypeString =
                preferences[PomodoroDataStoreKeys.TIMER_STYLE_KEY] ?: TimerStyle.REGULAR.name
            try {
                TimerStyle.valueOf(themeTypeString)
            } catch (_: IllegalArgumentException) {
                TimerStyle.REGULAR
            }
        }
    }

    suspend fun setTimerStyle(style: TimerStyle) {
        dataStore.edit { preferences ->
            preferences[PomodoroDataStoreKeys.TIMER_STYLE_KEY] = style.name
        }
    }
}