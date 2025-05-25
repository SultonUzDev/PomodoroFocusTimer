package com.sultonuzdev.pft.features.settings.presentation.utils

import com.sultonuzdev.pft.core.ui.theme.ThemeMode
import com.sultonuzdev.pft.core.ui.utils.UiIntent
import com.sultonuzdev.pft.core.util.Language


/**
 * Represents user actions for the Settings screen
 */
sealed class SettingsIntent : UiIntent {
    data class UpdatePomodoroMinutes(val minutes: Int) : SettingsIntent()
    data class UpdateShortBreakMinutes(val minutes: Int) : SettingsIntent()
    data class UpdateLongBreakMinutes(val minutes: Int) : SettingsIntent()
    data class UpdatePomodorosBeforeLongBreak(val count: Int) : SettingsIntent()
    data class UpdateVibrationEnabled(val enabled: Boolean) : SettingsIntent()
    data class UpdateSoundEnabled(val enabled: Boolean) : SettingsIntent()
    data class UpdateThemeMode(val themeMode: ThemeMode) : SettingsIntent()
    data class UpdateFocusModeEnabled(val enabled: Boolean) : SettingsIntent()
    data class UpdateLanguage(val language: Language) : SettingsIntent()
    data object ResetToDefaults : SettingsIntent()

}


