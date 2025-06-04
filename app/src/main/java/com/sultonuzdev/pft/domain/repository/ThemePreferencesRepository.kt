package com.sultonuzdev.pft.domain.repository

import com.sultonuzdev.pft.core.ui.theme.ThemeMode
import kotlinx.coroutines.flow.Flow

interface ThemePreferencesRepository {
    fun getThemeMode(): Flow<ThemeMode>
    suspend fun setThemeMode(themeMode: ThemeMode)
}