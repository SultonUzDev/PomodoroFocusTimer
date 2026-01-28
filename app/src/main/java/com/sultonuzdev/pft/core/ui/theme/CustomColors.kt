package com.sultonuzdev.pft.core.ui.theme


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Custom Colors for Timer Themes
 * Access via: MaterialTheme.customColors.studyBackground
 */



val LightColorScheme3 = lightColorScheme(
    primary = Color(0xFFFCA311),
    background = Color(0xFFE5E5E5),
    onBackground = Color(0xFF000000),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFFF5F5F5)
)

val DarkColorScheme3 = darkColorScheme(
    primary = Color(0xFFFCA311),
    background = Color(0xFF2A2B2D),
    onBackground = Color(0xFFE5E5E5),
    surface = Color(0xFF000000),
    onSurface = Color(0xFFFFFFFF)
)




// ============================================================================
// STUDY THEME COLORS
// ============================================================================

object CustomThemeColors {
    val studyDarkBackground: Color = Color(0xFF0A0A0A)
    val studyDarkPrimary: Color = Color(0xFFFF6B6B)
    val studyDarkText: Color = Color.White
    val studyDarkTextSecondary: Color = Color(0xFF666666)
    val studyDarkBorder: Color = Color(0xFF333333)

    // Light mode
    val studyLightBackground: Color = Color(0xFFF8F9FA)
    val studyLightPrimary: Color = Color(0xFFE74C3C)
    val studyLightText: Color = Color(0xFF2C3E50)
    val studyLightTextSecondary: Color = Color(0xFF6C757D)
    val studyLightBorder: Color = Color(0xFFDEE2E6)


    // Dark mode - Sepia/warm tones
    val readingDarkBackgroundStart: Color = Color(0xFF2D1B00)
    val readingDarkBackgroundEnd: Color = Color(0xFF1A0F00)
    val readingDarkBookStart: Color = Color(0xFFD4A574)
    val readingDarkBookEnd: Color = Color(0xFFC89B5E)
    val readingDarkText: Color = Color(0xFFF4E4C1)
    val readingDarkTitle: Color = Color(0xFFD4A574)
    val readingDarkSubtitle: Color = Color(0xFF8B7355)
    val readingDarkTimerText: Color = Color(0xFF2D1B00)
    val readingDarkStatValue: Color = Color(0xFFD4A574)
    val readingDarkStatLabel: Color = Color(0xFF8B7355)
    val readingDarkButtonBorder: Color = Color(0xFFD4A574)
    val readingDarkButtonText: Color = Color(0xFFD4A574)
    val readingDarkButtonPrimaryBg: Color = Color(0xFFD4A574)
    val readingDarkButtonPrimaryText: Color = Color(0xFF2D1B00)

    // Light mode - Cream/beige tones
    val readingLightBackgroundStart: Color = Color(0xFFFFF9E6)
    val readingLightBackgroundEnd: Color = Color(0xFFFEF5D9)
    val readingLightBookStart: Color = Color(0xFFFFE4B5)
    val readingLightBookEnd: Color = Color(0xFFFFD89B)
    val readingLightText: Color = Color(0xFF5D4E37)
    val readingLightTitle: Color = Color(0xFF8B6F47)
    val readingLightSubtitle: Color = Color(0xFFA0826D)
    val readingLightTimerText: Color = Color(0xFF5D4E37)
    val readingLightStatValue: Color = Color(0xFF8B6F47)
    val readingLightStatLabel: Color = Color(0xFFA0826D)
    val readingLightButtonBorder: Color = Color(0xFF8B6F47)
    val readingLightButtonText: Color = Color(0xFF8B6F47)
    val readingLightButtonPrimaryBg: Color = Color(0xFF8B6F47)
    val readingLightButtonPrimaryText: Color = Color(0xFFFFF9E6)


    // Dark mode - Purple gradient
    val meditationDarkBackgroundStart: Color = Color(0xFF2A2A40)
    val meditationDarkBackgroundEnd: Color = Color(0xFF1A1A2E)
    val meditationDarkPrimary: Color = Color(0xFF8A76FF)
    val meditationDarkBreatheText: Color = Color(0xFFA5A5D0)
    val meditationDarkText: Color = Color.White
    val meditationDarkDotInactive: Color = Color(0xFF3A3A50)
    val meditationDarkButtonBorder: Color = Color(0xFF4A4A60)

    // Light mode - Blue gradient
    val meditationLightBackgroundStart: Color = Color(0xFFF0F4FF)
    val meditationLightBackgroundEnd: Color = Color(0xFFE6EFFF)
    val meditationLightPrimary: Color = Color(0xFF6366F1)
    val meditationLightBreatheText: Color = Color(0xFF6B7FA8)
    val meditationLightText: Color = Color(0xFF2C3E50)
    val meditationLightDotInactive: Color = Color(0xFFD1D5F0)
    val meditationLightButtonBorder: Color = Color(0xFFC7CBDE)


    // Dark mode - Navy/blue
    val workDarkBackground: Color = Color(0xFF1A1D2E)
    val workDarkCardBackground: Color = Color(0xFF252941)
    val workDarkPrimary: Color = Color(0xFF3B82F6)
    val workDarkText: Color = Color.White
    val workDarkTextSecondary: Color = Color(0xFF9CA3AF)
    val workDarkBorder: Color = Color(0xFF374151)

    // Light mode - Clean white
    val workLightBackground: Color = Color(0xFFF5F7FA)
    val workLightCardBackground: Color = Color.White
    val workLightPrimary: Color = Color(0xFF3498DB)
    val workLightText: Color = Color(0xFF1A1D2E)
    val workLightTextSecondary: Color = Color(0xFF6C757D)
    val workLightBorder: Color = Color(0xFFE5E7EB)


    // Dark mode - VS Code style
    val codingDarkBackground: Color = Color(0xFF1E1E1E)
    val codingDarkTerminalBg: Color = Color(0xFF252526)
    val codingDarkPrimary: Color = Color(0xFF4EC9B0)
    val codingDarkText: Color = Color(0xFFD4D4D4)
    val codingDarkPrompt: Color = Color(0xFF4EC9B0)
    val codingDarkCommand: Color = Color(0xFF569CD6)
    val codingDarkComment: Color = Color(0xFF6A9955)
    val codingDarkString: Color = Color(0xFFCE9178)
    val codingDarkKeyword: Color = Color(0xFFC586C0)
    val codingDarkDotRed: Color = Color(0xFFFF5F56)
    val codingDarkDotYellow: Color = Color(0xFFFFBD2E)
    val codingDarkDotGreen: Color = Color(0xFF27C93F)

    // Light mode - Atom Light style
    val codingLightBackground: Color = Color(0xFFF5F5F5)
    val codingLightTerminalBg: Color = Color.White
    val codingLightPrimary: Color = Color(0xFF0184BC)
    val codingLightText: Color = Color(0xFF383A42)
    val codingLightPrompt: Color = Color(0xFF0184BC)
    val codingLightCommand: Color = Color(0xFF4078F2)
    val codingLightComment: Color = Color(0xFFA0A1A7)
    val codingLightString: Color = Color(0xFF50A14F)
    val codingLightKeyword: Color = Color(0xFFA626A4)
    val codingLightDotRed: Color = Color(0xFFFF5F56)
    val codingLightDotYellow: Color = Color(0xFFFFBD2E)
    val codingLightDotGreen: Color = Color(0xFF27C93F)
}


@Immutable
data class StudyColors(
    // Dark mode
    val background: Color,
    val primary: Color,
    val text: Color,
    val secondary: Color,
    val border: Color,
)


// ============================================================================
// READING THEME COLORS
// ============================================================================

@Immutable
data class ReadingColors(
    // Dark mode - Sepia/warm tones
    val backgroundStart: Color,
    val backgroundEnd: Color,
    val bookStart: Color,
    val bookEnd: Color,
    val text: Color,
    val title: Color,
    val subtitle: Color,
    val timerText: Color,
    val statValue: Color,
    val statLabel: Color,
    val buttonBorder: Color,
    val buttonText: Color,
    val buttonPrimaryBg: Color,
    val buttonPrimaryText: Color,

    )

// ============================================================================
// MEDITATION THEME COLORS
// ============================================================================

@Immutable
data class MeditationColors(
    // Dark mode - Purple gradient
    val backgroundStart: Color,
    val backgroundEnd: Color,
    val primary: Color,
    val breatheText: Color,
    val text: Color,
    val dotInactive: Color,
    val buttonBorder: Color,
)

// ============================================================================
// WORK THEME COLORS
// ============================================================================

@Immutable
data class WorkColors(
    // Dark mode - Navy/blue
    val background: Color,
    val cardBackground: Color,
    val primary: Color,
    val text: Color,
    val textSecondary: Color,
    val border: Color,
)

// ============================================================================
// CODING THEME COLORS
// ============================================================================

@Immutable
data class CodingColors(
    // Dark mode - VS Code style
    val background: Color,
    val terminalBg: Color,
    val primary: Color,
    val text: Color,
    val prompt: Color,
    val command: Color,
    val comment: Color,
    val string: Color,
    val keyword: Color,
    val dotRed: Color,
    val dotYellow: Color,
    val dotGreen: Color,
)

// ============================================================================
// COMBINED CUSTOM COLORS
// ============================================================================

@Immutable
data class CustomColors(
    val study: StudyColors,
    val reading: ReadingColors,
    val meditation: MeditationColors,
    val work: WorkColors,
    val coding: CodingColors
)


// ============================================================================
// COMPOSITION LOCAL
// ============================================================================

val LocalCustomColors = staticCompositionLocalOf { CustomThemeDarkColors }

val CustomThemeDarkColors = CustomColors(
    study = StudyColors(
        background = CustomThemeColors.studyDarkBackground,
        primary = CustomThemeColors.studyDarkPrimary,
        text = CustomThemeColors.studyDarkText,
        secondary = CustomThemeColors.studyDarkTextSecondary,
        border = CustomThemeColors.studyDarkBorder
    ),
    reading = ReadingColors(
        backgroundStart = CustomThemeColors.readingDarkBackgroundStart,
        backgroundEnd = CustomThemeColors.readingDarkBackgroundEnd,
        bookStart = CustomThemeColors.readingDarkBookStart,
        bookEnd = CustomThemeColors.readingDarkBookEnd,
        text = CustomThemeColors.readingDarkText,
        title = CustomThemeColors.readingDarkTitle,
        subtitle = CustomThemeColors.readingDarkSubtitle,
        timerText = CustomThemeColors.readingDarkTimerText,
        statValue = CustomThemeColors.readingDarkStatValue,
        statLabel = CustomThemeColors.readingDarkStatLabel,
        buttonBorder = CustomThemeColors.readingDarkButtonBorder,
        buttonText = CustomThemeColors.readingDarkButtonText,
        buttonPrimaryBg = CustomThemeColors.readingDarkButtonPrimaryBg,
        buttonPrimaryText = CustomThemeColors.readingDarkButtonPrimaryText
    ),

    meditation = MeditationColors(
        backgroundStart = CustomThemeColors.meditationDarkBackgroundStart,
        backgroundEnd = CustomThemeColors.meditationDarkBackgroundEnd,
        primary = CustomThemeColors.meditationDarkPrimary,
        breatheText = CustomThemeColors.meditationDarkBreatheText,
        text = CustomThemeColors.meditationDarkText,
        dotInactive = CustomThemeColors.meditationDarkDotInactive,
        buttonBorder = CustomThemeColors.meditationDarkButtonBorder
    ),
    work = WorkColors(
        background = CustomThemeColors.workDarkBackground,
        cardBackground = CustomThemeColors.workDarkCardBackground,
        primary = CustomThemeColors.workDarkPrimary,
        text = CustomThemeColors.workDarkText,
        textSecondary = CustomThemeColors.workDarkTextSecondary,
        border = CustomThemeColors.workDarkBorder
    ),
    coding = CodingColors(
        background = CustomThemeColors.codingDarkBackground,
        terminalBg = CustomThemeColors.codingDarkTerminalBg,
        primary = CustomThemeColors.codingDarkPrimary,
        text = CustomThemeColors.codingDarkText,
        prompt = CustomThemeColors.codingDarkPrompt,
        command = CustomThemeColors.codingDarkCommand,
        comment = CustomThemeColors.codingDarkComment,
        string = CustomThemeColors.codingDarkString,
        keyword = CustomThemeColors.codingDarkKeyword,
        dotRed = CustomThemeColors.codingDarkDotRed,
        dotYellow = CustomThemeColors.codingDarkDotYellow,
        dotGreen = CustomThemeColors.codingDarkDotGreen
    )
)

val CustomThemeLightColors = CustomColors(
    study = StudyColors(
        background = CustomThemeColors.studyLightBackground,
        primary = CustomThemeColors.studyLightPrimary,
        text = CustomThemeColors.studyLightText,
        secondary = CustomThemeColors.studyLightTextSecondary,
        border = CustomThemeColors.studyLightBorder
    ),
    reading = ReadingColors(
        backgroundStart = CustomThemeColors.readingLightBackgroundStart,
        backgroundEnd = CustomThemeColors.readingLightBackgroundEnd,
        bookStart = CustomThemeColors.readingLightBookStart,
        bookEnd = CustomThemeColors.readingLightBookEnd,
        text = CustomThemeColors.readingLightText,
        title = CustomThemeColors.readingLightTitle,
        subtitle = CustomThemeColors.readingLightSubtitle,
        timerText = CustomThemeColors.readingLightTimerText,
        statValue = CustomThemeColors.readingLightStatValue,
        statLabel = CustomThemeColors.readingLightStatLabel,
        buttonBorder = CustomThemeColors.readingLightButtonBorder,
        buttonText = CustomThemeColors.readingLightButtonText,
        buttonPrimaryBg = CustomThemeColors.readingLightButtonPrimaryBg,
        buttonPrimaryText = CustomThemeColors.readingLightButtonPrimaryText,
    ),
    meditation = MeditationColors(
        backgroundStart = CustomThemeColors.meditationLightBackgroundStart,
        backgroundEnd = CustomThemeColors.meditationLightBackgroundEnd,
        primary = CustomThemeColors.meditationLightPrimary,
        breatheText = CustomThemeColors.meditationLightBreatheText,
        text = CustomThemeColors.meditationLightText,
        dotInactive = CustomThemeColors.meditationLightDotInactive,
        buttonBorder = CustomThemeColors.meditationLightButtonBorder
    ),
    work = WorkColors(
        background = CustomThemeColors.workLightBackground,
        cardBackground = CustomThemeColors.workLightCardBackground,
        primary = CustomThemeColors.workLightPrimary,
        text = CustomThemeColors.workLightText,
        textSecondary = CustomThemeColors.workLightTextSecondary,
        border = CustomThemeColors.workLightBorder
    ),
    coding = CodingColors(
        background = CustomThemeColors.codingLightBackground,
        terminalBg = CustomThemeColors.codingLightTerminalBg,
        primary = CustomThemeColors.codingLightPrimary,
        text = CustomThemeColors.codingLightText,
        prompt = CustomThemeColors.codingLightPrompt,
        command = CustomThemeColors.codingLightCommand,
        comment = CustomThemeColors.codingLightComment,
        string = CustomThemeColors.codingLightString,
        keyword = CustomThemeColors.codingLightKeyword,
        dotRed = CustomThemeColors.codingLightDotRed,
        dotYellow = CustomThemeColors.codingLightDotYellow,
        dotGreen = CustomThemeColors.codingLightDotGreen
    ),
)


val MaterialTheme.customColors: CustomColors
    @Composable
    get() = LocalCustomColors.current

