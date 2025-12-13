package com.sultonuzdev.pft.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider


@Composable
fun PomodoroTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val customTypography = CustomTypography(
        study = getStudyTypography(),
        reading = getReadingTypography(),
        meditation = getMeditationTypography(),
        work = getWorkTypography(),
        coding = getCodingTypography()
    )

    val customColors = if (darkTheme) CustomThemeDarkColors else CustomThemeLightColors

    CompositionLocalProvider(
        LocalCustomColors provides customColors,
        LocalCustomTypography provides customTypography
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = PomodoroTypography,
            content = content
        )
    }

}