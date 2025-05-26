package com.sultonuzdev.pft.core.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.sultonuzdev.pft.core.ui.theme.PomodoroAppTheme
import com.sultonuzdev.pft.core.ui.theme.ThemeMode
import com.sultonuzdev.pft.features.settings.domain.repository.ThemePreferencesRepository


@Composable
fun PomodoroRootApp(
    themePreferencesRepository: ThemePreferencesRepository,
    content: @Composable () -> Unit
) {





    val themeMode = themePreferencesRepository.getThemeMode()
        .collectAsState(initial = ThemeMode.SYSTEM).value

    // Determine dark/light theme based on theme mode
    val isDarkTheme = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
    }

    PomodoroAppTheme(darkTheme = isDarkTheme) {
        content()
    }
}