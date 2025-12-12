package com.sultonuzdev.pft.data.repository

import com.sultonuzdev.pft.core.enums.TimerStyle
import com.sultonuzdev.pft.core.ui.theme.ThemeMode
import com.sultonuzdev.pft.data.preferences.PreferencesManager
import com.sultonuzdev.pft.domain.model.PomodoroTimerSettings
import com.sultonuzdev.pft.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class SettingsRepositoryImpl(
    private val preferencesManager: PreferencesManager
) : SettingsRepository {
    override fun getDefaultSettings(): Flow<PomodoroTimerSettings> {
        return preferencesManager.getSettings()
    }

    override suspend fun updateSettings(settings: PomodoroTimerSettings) {
        preferencesManager.updateSettings(settings)
    }


    override fun getThemeMode(): Flow<ThemeMode> {
        return preferencesManager.getThemeMode()
    }

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        preferencesManager.setThemeMode(themeMode)
    }

    override fun getTimerStyle(): Flow<TimerStyle> {
        return preferencesManager.getTimerStyle()
    }

    override suspend fun setTimerStyle(style: TimerStyle) {
        preferencesManager.setTimerStyle(style)
    }


}