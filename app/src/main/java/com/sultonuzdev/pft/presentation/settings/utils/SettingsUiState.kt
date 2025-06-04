package com.sultonuzdev.pft.presentation.settings.utils

import com.sultonuzdev.pft.core.ui.theme.ThemeMode
import com.sultonuzdev.pft.core.ui.utils.UiState
import com.sultonuzdev.pft.core.util.Language
import com.sultonuzdev.pft.domain.model.TimerSettings

/**
 * Represents the UI state for the Settings screen
 */
data class SettingsUiState(
    val settings: TimerSettings = TimerSettings(),
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val selectedLanguage: Language = Language.ENGLISH,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) : UiState