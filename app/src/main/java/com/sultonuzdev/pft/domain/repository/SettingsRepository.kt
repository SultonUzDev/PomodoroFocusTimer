package com.sultonuzdev.pft.domain.repository

import com.sultonuzdev.pft.core.enums.TimerStyle
import com.sultonuzdev.pft.core.ui.theme.ThemeMode
import com.sultonuzdev.pft.domain.model.PomodoroTimerSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {



     fun getDefaultSettings(): Flow<PomodoroTimerSettings>
    suspend fun updateSettings(settings: PomodoroTimerSettings)

     fun getThemeMode(): Flow<ThemeMode>
    suspend fun setThemeMode(themeMode: ThemeMode)


    fun getTimerStyle(): Flow<TimerStyle>
    suspend fun setTimerStyle(style: TimerStyle)
}