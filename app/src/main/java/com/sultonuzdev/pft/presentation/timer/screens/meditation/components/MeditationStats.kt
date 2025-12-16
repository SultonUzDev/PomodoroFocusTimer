package com.sultonuzdev.pft.presentation.timer.screens.meditation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sultonuzdev.pft.core.ui.theme.PomodoroTheme
import com.sultonuzdev.pft.core.ui.theme.customColors
import com.sultonuzdev.pft.core.ui.theme.customTypography
import com.sultonuzdev.pft.core.util.AppPreview
import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.core.util.formatAsDuration


/**
 * Meditation statistics with zen-inspired design
 */
@Composable
fun MeditationStats(
    completedSessions: Int,
    timeSpent: Int,
    currentSession: Int,
    currentType: TimerType,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {



        // Stats Row with peaceful emojis
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Mindful Sessions
            MeditationStat(
                emoji = "🧘",
                value = completedSessions.toString(),
                label = "Mindful"
            )

            // Time in Peace
            MeditationStat(
                emoji = "⏱️",
                value = timeSpent.formatAsDuration(),
                label = "Peace"
            )

            // Breath Cycles
            MeditationStat(
                emoji = "🌸",
                value = "$currentSession/4",
                label = "Cycles"
            )
        }

        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.customColors.meditation.buttonBorder)

        // Timer Type Selection with peaceful dots
        MeditationTimerTypes(currentType = currentType)


    }
}

/**
 * Single stat item with gentle pulsing animation
 */
@Composable
private fun MeditationStat(
    emoji: String,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {


    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        // Emoji with gentle pulse
        Text(
            text = emoji,
            fontSize = 24.sp,
        )

        // Value
        Text(
            text = value,
            style = MaterialTheme.customTypography.meditation.breatheText,
            color = MaterialTheme.customColors.meditation.primary,
            maxLines = 1
        )

        // Label
        Text(
            text = label,
            style = MaterialTheme.customTypography.meditation.breatheText,
            color = MaterialTheme.customColors.meditation.text.copy(alpha = 0.7f),
            maxLines = 1
        )
    }
}

/**
 * Timer type indicator with zen dots
 */
@Composable
private fun MeditationTimerTypes(
    currentType: TimerType,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MeditationTimerTypeItem(
            text = "Pomodoro",
            isSelected = currentType == TimerType.POMODORO
        )

        ZenDivider()

        MeditationTimerTypeItem(
            text = "Short Break",
            isSelected = currentType == TimerType.SHORT_BREAK
        )

        ZenDivider()

        MeditationTimerTypeItem(
            text = "Long Break",
            isSelected = currentType == TimerType.LONG_BREAK
        )
    }
}

/**
 * Individual timer type with gentle styling
 */
@Composable
private fun MeditationTimerTypeItem(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {


    Text(
        text = text,
        style = MaterialTheme.customTypography.meditation.breatheText.copy(
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        ),
        color = if (isSelected)
            MaterialTheme.customColors.meditation.primary
        else
            MaterialTheme.customColors.meditation.primary.copy(alpha = 0.5f),
        modifier = modifier
    )
}

/**
 * Decorative zen dot divider
 */
@Composable
private fun ZenDivider(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(4.dp)
            .clip(CircleShape)
            .background(MaterialTheme.customColors.meditation.dotInactive)
    )
}

@AppPreview
@Composable
private fun MeditationStatsPreview() {
    PomodoroTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.customColors.meditation.backgroundEnd)
                .padding(16.dp)
        ) {
            MeditationStats(
                completedSessions = 5,
                timeSpent = 125,
                currentSession = 3,
                currentType = TimerType.POMODORO
            )
        }
    }
}

@AppPreview
@Composable
private fun MeditationStatsBreakPreview() {
    PomodoroTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.customColors.meditation.backgroundEnd)
                .padding(16.dp)
        ) {
            MeditationStats(
                completedSessions = 8,
                timeSpent = 200,
                currentSession = 2,
                currentType = TimerType.SHORT_BREAK
            )
        }
    }
}

@AppPreview
@Composable
private fun MeditationStatsLongBreakPreview() {
    PomodoroTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.customColors.meditation.backgroundEnd)
                .padding(16.dp)
        ) {
            MeditationStats(
                completedSessions = 12,
                timeSpent = 300,
                currentSession = 4,
                currentType = TimerType.LONG_BREAK
            )
        }
    }
}
