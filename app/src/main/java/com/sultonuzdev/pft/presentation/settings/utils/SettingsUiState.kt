package com.sultonuzdev.pft.presentation.settings.utils

import com.sultonuzdev.pft.core.ui.theme.ThemeMode
import com.sultonuzdev.pft.core.ui.utils.UiState
import com.sultonuzdev.pft.domain.model.PomodoroTimerSettings
import org.intellij.lang.annotations.Language

/**
 * Represents the UI state for the Settings screen
 */
data class SettingsUiState(
    val settings: PomodoroTimerSettings = PomodoroTimerSettings(),
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) : UiState