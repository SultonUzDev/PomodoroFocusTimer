package com.sultonuzdev.pft.core.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color



val statsIconColor = Color(0xFFFF5722)     // Deep Orange
val settingsIconColor = Color(0xFF2196F3)  // Blue
// Light Theme Colors

val LightColorScheme = lightColorScheme(
    primary = Color(0xFFFF5757),         // Vibrant Coral for Pomodoro
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFECEA), // Light Coral background
    onPrimaryContainer = Color(0xFF690013),

    secondary = Color(0xFF4ECDC4),       // Teal for Short Break
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCDF7F3),
    onSecondaryContainer = Color(0xFF003936),

    tertiary = Color(0xFF7B69EE),        // Purple for Long Break
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFEAE5FF),
    onTertiaryContainer = Color(0xFF2F2274),

    background = Color(0xFFFAFAFA),
    onBackground = Color(0xFF1C1B1F),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1C1B1F),

    surfaceVariant = Color(0xFFF3F3F3),
    onSurfaceVariant = Color(0xFF49454F),
    outline = Color(0xFFD8D8D8),
    error = Color(0xFFB3261E),
    onError = Color.White,
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B),
    inverseSurface = Color(0xFF313033),
    inverseOnSurface = Color(0xFFF4EFF4)
)

// Dark Theme Colors
val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFF6B6B),         // Brighter Coral for Pomodoro
    onPrimary = Color.White,
    primaryContainer = Color(0xFF690013),
    onPrimaryContainer = Color(0xFFFFECEA),

    secondary = Color(0xFF72EFEA),       // Bright Teal for Short Break
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF003936),
    onSecondaryContainer = Color(0xFFCDF7F3),

    tertiary = Color(0xFF988FFF),        // Bright Purple for Long Break
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFF2F2274),
    onTertiaryContainer = Color(0xFFEAE5FF),

    background = Color(0xFF121212),
    onBackground = Color(0xFFE5E1E6),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFE5E1E6),

    surfaceVariant = Color(0xFF2D2D2D),
    onSurfaceVariant = Color(0xFFCAC4D0),
    outline = Color(0xFF444444),
    error = Color(0xFFCF6679),
    onError = Color.Black,
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF9DEDC),
    inverseSurface = Color(0xFFE5E1E6),
    inverseOnSurface = Color(0xFF1C1B1F)

)