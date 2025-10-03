package com.sultonuzdev.pft.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.sultonuzdev.pft.core.ui.theme.ThemeMode
import com.sultonuzdev.pft.core.util.Constants.DEFAULT_LONG_BREAK_MINUTES
import com.sultonuzdev.pft.core.util.Constants.DEFAULT_POMODORO_CYCLE_LENGTH
import com.sultonuzdev.pft.core.util.Constants.DEFAULT_POMODORO_MINUTES
import com.sultonuzdev.pft.core.util.Constants.DEFAULT_SHORT_BREAK_MINUTES
import com.sultonuzdev.pft.data.db.dao.PomodoroDao
import com.sultonuzdev.pft.data.mapper.toDomainModel
import com.sultonuzdev.pft.data.mapper.toEntity
import com.sultonuzdev.pft.domain.model.Pomodoro
import com.sultonuzdev.pft.domain.model.PomodoroTimerSettings
import com.sultonuzdev.pft.domain.repository.PomodoroRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

/**
 * Implementation of SessionRepository that uses Room database for persistence
 */
class PomodoroRepositoryImpl @Inject constructor(
    private val pomodoroDao: PomodoroDao,
    private val dataStore: DataStore<Preferences>
) : PomodoroRepository {

    companion object {
        private val POMODORO_MINUTES = intPreferencesKey("pomodoro_minutes")
        private val SHORT_BREAK_MINUTES = intPreferencesKey("short_break_minutes")
        private val LONG_BREAK_MINUTES = intPreferencesKey("long_break_minutes")
        private val POMODOROS_BEFORE_LONG_BREAK = intPreferencesKey("pomodoros_before_long_break")
        private val VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
        private val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        private val FOCUS_MODE_ENABLED = booleanPreferencesKey("focus_mode_enabled")

        private val THEME_MODE_KEY = stringPreferencesKey("theme_mode")

    }

    override fun getSettings(): Flow<PomodoroTimerSettings> {
        return dataStore.data.map { preferences ->
            PomodoroTimerSettings(
                pomodoroMinutes = preferences[POMODORO_MINUTES] ?: DEFAULT_POMODORO_MINUTES,
                shortBreakMinutes = preferences[SHORT_BREAK_MINUTES] ?: DEFAULT_SHORT_BREAK_MINUTES,
                longBreakMinutes = preferences[LONG_BREAK_MINUTES] ?: DEFAULT_LONG_BREAK_MINUTES,
                pomodoroCycleLength = preferences[POMODOROS_BEFORE_LONG_BREAK]
                    ?: DEFAULT_POMODORO_CYCLE_LENGTH,
                vibrationEnabled = preferences[VIBRATION_ENABLED] ?: true,
                soundEnabled = preferences[SOUND_ENABLED] ?: true,
                enableFocusMode = preferences[FOCUS_MODE_ENABLED] ?: false
            )
        }
    }

    override suspend fun updateSettings(settings: PomodoroTimerSettings) {
        dataStore.edit { preferences ->
            preferences[POMODORO_MINUTES] = settings.pomodoroMinutes
            preferences[SHORT_BREAK_MINUTES] = settings.shortBreakMinutes
            preferences[LONG_BREAK_MINUTES] = settings.longBreakMinutes
            preferences[POMODOROS_BEFORE_LONG_BREAK] = settings.pomodoroCycleLength
            preferences[VIBRATION_ENABLED] = settings.vibrationEnabled
            preferences[SOUND_ENABLED] = settings.soundEnabled
            preferences[FOCUS_MODE_ENABLED] = settings.enableFocusMode
        }
    }

    override suspend fun savePomodoro(pomodoro: Pomodoro) {
        pomodoroDao.insertSession(pomodoro.toEntity())
    }

    override fun getAllPomodoros(): Flow<List<Pomodoro>> {
        return pomodoroDao.getAllSessions().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getPomodoroByDate(date: LocalDate): Flow<List<Pomodoro>> {
        return pomodoroDao.getSessionsByDate(date.toString()).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getWeeklyStats(
        startOfTheWeek: LocalDate,
        endOfTheWeek: LocalDate
    ): Flow<List<Pomodoro>> {
        return pomodoroDao.getSessionsByDateRange(
            startOfTheWeek.toString(),
            endOfTheWeek.toString()
        )
            .map { it.toDomainModel() }
    }


    override fun getThemeMode(): Flow<ThemeMode> {
        return dataStore.data.map { preferences ->
            val themeModeString = preferences[THEME_MODE_KEY] ?: ThemeMode.SYSTEM.name
            try {
                ThemeMode.valueOf(themeModeString)
            } catch (_: IllegalArgumentException) {
                ThemeMode.SYSTEM
            }
        }
    }

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        dataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] = themeMode.name
        }
    }
}