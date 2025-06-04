// com.sultonuzdev.pft.features.stats.presentation.SessionHistoryItem.kt
package com.sultonuzdev.pft.presentation.stats.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Weekend
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.domain.model.Pomodoro
import java.time.format.DateTimeFormatter

/**
 * List item for a timer session in the history
 */



@Composable
fun SessionHistoryItem(
    session: Pomodoro,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Session icon with improved styling
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    when (session.type) {
                        TimerType.POMODORO -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        TimerType.SHORT_BREAK -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f)
                        TimerType.LONG_BREAK -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = when (session.type) {
                    TimerType.POMODORO -> if (session.completed) Icons.Default.Check else Icons.Default.Close
                    TimerType.SHORT_BREAK -> Icons.Default.Coffee
                    TimerType.LONG_BREAK -> Icons.Default.Weekend
                },
                contentDescription = null,
                tint = when (session.type) {
                    TimerType.POMODORO -> MaterialTheme.colorScheme.primary
                    TimerType.SHORT_BREAK -> MaterialTheme.colorScheme.tertiary
                    TimerType.LONG_BREAK -> MaterialTheme.colorScheme.secondary
                },
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = when (session.type) {
                    TimerType.POMODORO -> "Pomodoro"
                    TimerType.SHORT_BREAK -> "Short Break"
                    TimerType.LONG_BREAK -> "Long Break"
                },
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
            )

            Text(
                text = "${session.durationMinutes} minutes ${if (session.completed) "completed" else "interrupted"}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = session.startTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = session.startTime.format(DateTimeFormatter.ofPattern("E, MMM d")),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}