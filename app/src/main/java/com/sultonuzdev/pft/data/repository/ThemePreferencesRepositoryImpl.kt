package com.sultonuzdev.pft.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.sultonuzdev.pft.domain.repository.ThemePreferencesRepository
import com.sultonuzdev.pft.core.ui.theme.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ThemePreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ThemePreferencesRepository {

    companion object {
        private val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
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