package com.sultonuzdev.pft.presentation.settings.utils

import com.sultonuzdev.pft.core.ui.utils.UiEffect

/**
 * Represents one-time events for the Settings screen
 */
sealed class SettingsEffect : UiEffect {
    data class ShowMessage(val message: String) : SettingsEffect()
}