package com.sultonuzdev.pft.presentation.timer.screens.reading.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
 * Reading statistics with emojis
 */
@Composable
fun ReadingStats(
    completedSession: Int,
    timeSpent: Int,
    sessions: Int,
    modifier: Modifier = Modifier,
    currentType: TimerType
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Row(

            modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Pages Read
            ReadingStat(
                emoji = "📖",
                value = completedSession.toString(),
                label = "Completed",
            )

            // Time Spent
            ReadingStat(
                emoji = "⏱",
                value = timeSpent.formatAsDuration(),
                label = "Time",

                )

            // Sessions
            ReadingStat(
                emoji = "📚",
                value = "$sessions/4",
                label = "Sessions",

                )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            TimerTypeText(
                text = "Pomodoro",
                isSelected = currentType == TimerType.POMODORO,
            )
            TimerTypeText(
                text = "Short Break",
                isSelected = currentType == TimerType.SHORT_BREAK,
            )
            TimerTypeText(
                text = "Long Break",
                isSelected = currentType == TimerType.LONG_BREAK,


                )
        }
    }


}

@Composable
private fun TimerTypeText(
    text: String,
    isSelected: Boolean,
) {
    Text(
        text = text,
        style = MaterialTheme.customTypography.reading.statLabel.copy(fontWeight = if (isSelected) FontWeight.SemiBold else MaterialTheme.customTypography.reading.statLabel.fontWeight),
        color = if (isSelected)
            MaterialTheme.customColors.reading.text
        else
            MaterialTheme.customColors.reading.buttonBorder
    )
}

/**
 * Single stat item with emoji
 */
@Composable
private fun ReadingStat(
    emoji: String,
    value: String,
    label: String,

    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = emoji, fontSize = 24.sp
        )
        Text(
            text = value,
            style = MaterialTheme.customTypography.reading.statValue,
            color = MaterialTheme.customColors.reading.statValue
        )
        Text(
            text = label,
            style = MaterialTheme.customTypography.reading.statLabel,
            color = MaterialTheme.customColors.reading.statLabel
        )
    }
}

@AppPreview
@Composable
private fun ReadingStatsPreview() {
    PomodoroTheme {
        ReadingStats(
            completedSession = 3,
            timeSpent = 75,
            sessions = 3,
            currentType = TimerType.LONG_BREAK
        )
    }
}