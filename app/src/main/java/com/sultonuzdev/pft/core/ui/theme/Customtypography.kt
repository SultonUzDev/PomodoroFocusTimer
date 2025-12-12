package com.sultonuzdev.pft.core.ui.theme


import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Custom Typography for Timer Themes
 * Access via: MaterialTheme.customTypography.studyTimer
 */

// ============================================================================
// STUDY THEME TYPOGRAPHY
// ============================================================================

@Immutable
data class StudyTypography(
    val timer: TextStyle,           // 160.sp, Bold, -8sp spacing
    val statValue: TextStyle,       // 36.sp, Bold
    val statLabel: TextStyle,       // 12.sp, Medium, Uppercase
    val buttonIcon: TextStyle,      // 28-40.sp
    val buttonLabel: TextStyle      // 9.sp, Medium, Uppercase
)

@Composable
fun getStudyTypography() = StudyTypography(
    timer = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 100.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp
    ),
    statValue = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    ),
    statLabel = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 1.5.sp
    ),
    buttonIcon = TextStyle(
        fontSize = 28.sp
    ),
    buttonLabel = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 9.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.5.sp
    )
)

// ============================================================================
// READING THEME TYPOGRAPHY
// ============================================================================

@Immutable
data class ReadingTypography(
    val title: TextStyle,           // 32.sp, Light, Serif
    val subtitle: TextStyle,        // 15.sp, Italic, Serif
    val timer: TextStyle,           // 80.sp, Bold, Sans
    val pageStatus: TextStyle,      // 14.sp, Regular
    val statValue: TextStyle,       // 24.sp, SemiBold, Serif
    val statLabel: TextStyle,       // 12.sp, Regular, Serif
    val button: TextStyle,          // 14.sp, Medium
    val statusText: TextStyle       // 14.sp, Italic
)

@Composable
fun getReadingTypography() = ReadingTypography(
    title = TextStyle(
        fontFamily = LibreBaskervilleFontFamily,
        fontSize = 32.sp,
        fontWeight = FontWeight.Light
    ),
    subtitle = TextStyle(
        fontFamily = LibreBaskervilleFontFamily,
        fontSize = 15.sp,
        fontStyle = FontStyle.Italic
    ),
    timer = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 80.sp,
        fontWeight = FontWeight.Bold
    ),
    pageStatus = TextStyle(
        fontFamily = LibreBaskervilleFontFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal
    ),
    statValue = TextStyle(
        fontFamily = LibreBaskervilleFontFamily,
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold
    ),
    statLabel = TextStyle(
        fontFamily = LibreBaskervilleFontFamily,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal
    ),
    button = TextStyle(
        fontFamily = LibreBaskervilleFontFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium
    ),
    statusText = TextStyle(
        fontFamily = LibreBaskervilleFontFamily,
        fontSize = 14.sp,
        fontStyle = FontStyle.Italic
    )
)

// ============================================================================
// MEDITATION THEME TYPOGRAPHY
// ============================================================================

@Immutable
data class MeditationTypography(
    val timer: TextStyle,           // 100.sp, Light (!)
    val breatheText: TextStyle,     // 18.sp, Regular
    val buttonIcon: TextStyle       // 24.sp
)

@Composable
fun getMeditationTypography() = MeditationTypography(
    timer = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 80.sp,
        fontWeight = FontWeight.SemiBold  // KEY: Light weight!
    ),
    breatheText = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 18.sp,
        fontWeight = FontWeight.Normal
    ),
    buttonIcon = TextStyle(
        fontSize = 24.sp
    )
)

// ============================================================================
// WORK THEME TYPOGRAPHY
// ============================================================================

@Immutable
data class WorkTypography(
    val title: TextStyle,           // 28.sp, Bold
    val timer: TextStyle,           // 80.sp, Bold
    val sessionLabel: TextStyle,    // 14.sp, Regular, Uppercase
    val statLabel: TextStyle,       // 15.sp, Regular
    val statValue: TextStyle,       // 26.sp, Bold
    val button: TextStyle           // 15.sp, SemiBold
)

@Composable
fun getWorkTypography() = WorkTypography(
    title = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold
    ),
    timer = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 80.sp,
        fontWeight = FontWeight.Bold
    ),
    sessionLabel = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 1.5.sp
    ),
    statLabel = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal
    ),
    statValue = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold
    ),
    button = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold
    )
)

// ============================================================================
// CODING THEME TYPOGRAPHY
// ============================================================================

@Immutable
data class CodingTypography(
    val timer: TextStyle,           // 100.sp, Bold, Mono
    val terminal: TextStyle,        // 14.sp, Regular, Mono
    val comment: TextStyle,         // 12.sp, Regular, Mono
    val statKey: TextStyle,         // 12.sp, Regular, Mono
    val statValue: TextStyle,       // 22.sp, SemiBold, Mono
    val button: TextStyle           // 13.sp, Regular, Mono
)

@Composable
fun getCodingTypography() = CodingTypography(
    timer = TextStyle(
        fontFamily = JetBrainsMonoFontFamily,
        fontSize = 100.sp,
        fontWeight = FontWeight.Bold
    ),
    terminal = TextStyle(
        fontFamily = JetBrainsMonoFontFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal
    ),
    comment = TextStyle(
        fontFamily = JetBrainsMonoFontFamily,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal
    ),
    statKey = TextStyle(
        fontFamily = JetBrainsMonoFontFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal
    ),
    statValue = TextStyle(
        fontFamily = JetBrainsMonoFontFamily,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold
    ),
    button = TextStyle(
        fontFamily = JetBrainsMonoFontFamily,
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal
    )
)

// ============================================================================
// COMBINED CUSTOM TYPOGRAPHY
// ============================================================================

@Immutable
data class CustomTypography(
    val study: StudyTypography,
    val reading: ReadingTypography,
    val meditation: MeditationTypography,
    val work: WorkTypography,
    val coding: CodingTypography
)

// ============================================================================
// COMPOSITION LOCAL
// ============================================================================

val LocalCustomTypography = staticCompositionLocalOf {
    CustomTypography(
        study = StudyTypography(
            timer = TextStyle.Default,
            statValue = TextStyle.Default,
            statLabel = TextStyle.Default,
            buttonIcon = TextStyle.Default,
            buttonLabel = TextStyle.Default
        ),
        reading = ReadingTypography(
            title = TextStyle.Default,
            subtitle = TextStyle.Default,
            timer = TextStyle.Default,
            pageStatus = TextStyle.Default,
            statValue = TextStyle.Default,
            statLabel = TextStyle.Default,
            button = TextStyle.Default,
            statusText = TextStyle.Default
        ),
        meditation = MeditationTypography(
            timer = TextStyle.Default,
            breatheText = TextStyle.Default,
            buttonIcon = TextStyle.Default
        ),
        work = WorkTypography(
            title = TextStyle.Default,
            timer = TextStyle.Default,
            sessionLabel = TextStyle.Default,
            statLabel = TextStyle.Default,
            statValue = TextStyle.Default,
            button = TextStyle.Default
        ),
        coding = CodingTypography(
            timer = TextStyle.Default,
            terminal = TextStyle.Default,
            comment = TextStyle.Default,
            statKey = TextStyle.Default,
            statValue = TextStyle.Default,
            button = TextStyle.Default
        )
    )
}

// ============================================================================
// MATERIAL THEME EXTENSION
// ============================================================================

/**
 * Access custom typography via MaterialTheme
 *
 * Usage:
 * ```
 * val timerStyle = MaterialTheme.customTypography.study.timer
 * val titleStyle = MaterialTheme.customTypography.reading.title
 * ```
 */
val MaterialTheme.customTypography: CustomTypography
    @Composable
    get() = LocalCustomTypography.current

// ============================================================================
// USAGE EXAMPLES
// ============================================================================

/**
 * Example 1: Direct access
 * ```
 * Text(
 *     text = "25:00",
 *     style = MaterialTheme.customTypography.study.timer
 * )
 * ```
 *
 * Example 2: In a composable
 * ```
 * @Composable
 * fun StudyTimerScreen() {
 *     val typography = MaterialTheme.customTypography.study
 *
 *     Column {
 *         Text(
 *             text = "25:00",
 *             style = typography.timer
 *         )
 *         Text(
 *             text = "4",
 *             style = typography.statValue
 *         )
 *         Text(
 *             text = "COMPLETED",
 *             style = typography.statLabel
 *         )
 *     }
 * }
 * ```
 *
 * Example 3: Reading theme with serif fonts
 * ```
 * @Composable
 * fun ReadingTimerScreen() {
 *     val typography = MaterialTheme.customTypography.reading
 *
 *     Column {
 *         Text(
 *             text = "Reading Session",
 *             style = typography.title  // Libre Baskerville, Light
 *         )
 *         Text(
 *             text = "Deep work through literature",
 *             style = typography.subtitle  // Italic serif
 *         )
 *         Text(
 *             text = "25:00",
 *             style = typography.timer  // Roboto, Bold
 *         )
 *     }
 * }
 * ```
 *
 * Example 4: Meditation with light weight
 * ```
 * Text(
 *     text = "25:00",
 *     style = MaterialTheme.customTypography.meditation.timer  // Light weight!
 * )
 * ```
 *
 * Example 5: Coding with monospace
 * ```
 * val typography = MaterialTheme.customTypography.coding
 *
 * Text(
 *     text = "25:00",
 *     style = typography.timer  // JetBrains Mono
 * )
 * Text(
 *     text = "$ ./focus --mode=coding",
 *     style = typography.terminal  // Monospace
 * )
 * ```
 */