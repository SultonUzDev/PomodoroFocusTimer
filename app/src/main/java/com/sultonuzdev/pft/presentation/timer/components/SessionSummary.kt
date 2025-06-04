package com.sultonuzdev.pft.presentation.timer.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sultonuzdev.pft.core.ui.theme.PomodoroAppTheme
import com.sultonuzdev.pft.core.util.formatFocusTime

@Preview(showBackground = true)
@Composable
private fun SessionSummaryPreview() {
    PomodoroAppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Different states to preview
            SessionSummary(
                currentSessionPomodoros = 0,
                pomodoroCycleLength = 4,
                todayPomodoros = 0,
                todayFocusTimeMinutes = 0
            )
            SessionSummary(
                currentSessionPomodoros = 2,
                pomodoroCycleLength = 4,
                todayPomodoros = 6,
                todayFocusTimeMinutes = 150
            )
            SessionSummary(
                currentSessionPomodoros = 4,
                pomodoroCycleLength = 4,
                todayPomodoros = 8,
                todayFocusTimeMinutes = 200
            )

            // Optional explanation for new users
            if (true) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    )
                ) {
                    Text(
                        text = "💡 Complete 4 pomodoros to earn a long break!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        modifier = Modifier.padding(12.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

/**
 * Enhanced SessionSummary with clearer session progress explanation
 */
@Composable
fun SessionSummary(
    currentSessionPomodoros: Int,
    pomodoroCycleLength: Int,
    todayPomodoros: Int,
    todayFocusTimeMinutes: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .animateContentSize()
            .clip(shape = MaterialTheme.shapes.medium),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp,
            pressedElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            modifier = Modifier
                .clip(shape = MaterialTheme.shapes.medium)
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Current Session Progress with better explanation
            SessionProgressItem(
                modifier = Modifier.weight(1f),
                currentPomodoros = currentSessionPomodoros,
                targetPomodoros = pomodoroCycleLength,
                color = MaterialTheme.colorScheme.primary,
            )

            VerticalDivider(
                modifier = Modifier
                    .height(48.dp)
                    .padding(horizontal = 8.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            )

            // Today's Pomodoros
            SummaryItem(
                modifier = Modifier.weight(1f),
                value = "$todayPomodoros",
                emoji = "🍅",
                label = "Today",
                color = MaterialTheme.colorScheme.secondary
            )

            VerticalDivider(
                modifier = Modifier
                    .height(48.dp)
                    .padding(horizontal = 8.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            )

            // Today's Focus Time
            SummaryItem(
                modifier = Modifier.weight(1f),
                value = todayFocusTimeMinutes.formatFocusTime(),
                label = "Focus Time",
                color = MaterialTheme.colorScheme.tertiary,
                icon = "⏱️"
            )
        }
    }
}

@Composable
private fun SessionProgressItem(
    currentPomodoros: Int,
    targetPomodoros: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Progress value with context
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$currentPomodoros",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = color,
            )
            Text(
                text = "/$targetPomodoros",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            )
        }

        // Dynamic label based on progress
        val labelText = when (currentPomodoros) {
            0 -> "Start Session"
            targetPomodoros -> "Long Break!"
            else -> "To Long Break"
        }

        Text(
            text = labelText,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 2.dp),
        )
    }
}

@Composable
private fun SummaryItem(
    value: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
    emoji: String? = null,
    icon: String? = null,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Text(
                    text = icon,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 2.dp)
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = color,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (emoji != null) {
                Text(
                    text = " $emoji",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                    color = color,
                )
            }
        }

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 2.dp),
        )
    }
}