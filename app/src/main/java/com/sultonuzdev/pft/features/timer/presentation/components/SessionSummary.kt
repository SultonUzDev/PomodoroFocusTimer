package com.sultonuzdev.pft.features.timer.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sultonuzdev.pft.core.ui.theme.PomodoroAppTheme


@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun SessionSummaryPreview() {
    PomodoroAppTheme {
        SessionSummary(completedPomodoros = 7)
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SessionSummaryDarkPreview() {
    PomodoroAppTheme {
        SessionSummary(completedPomodoros = 7)
    }
}

@Composable
fun SessionSummary(
    completedPomodoros: Int,
    modifier: Modifier = Modifier
) {
    // Calculate completed sessions (every 4 Pomodoros)
    val completedSessions = completedPomodoros / 4
    // Calculate Pomodoros in current session (remainder after dividing by 4)
    val currentSessionPomodoros = completedPomodoros % 4

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Completed sessions
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "$completedSessions",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
            Text(
                text = "sessions today",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Add a separator if both metrics are shown
        if (currentSessionPomodoros > 0) {
            Text(
                text = "/",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Current session progress indicator (0-3 Pomodoros completed in current session)
        if (currentSessionPomodoros > 0 || completedSessions == 0) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Visual indicator of Pomodoros in current session
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(4) { index ->
                        val isCompleted = index < currentSessionPomodoros
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    color = if (isCompleted)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurfaceVariant,
                                    shape = CircleShape
                                )
                        )
                    }
                }

                Text(
                    text = "$currentSessionPomodoros/4 in current session",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}