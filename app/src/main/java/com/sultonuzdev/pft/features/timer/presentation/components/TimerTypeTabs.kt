// com.sultonuzdev.pft.features.timer.presentation.components.TimerTypeTabs.kt
package com.sultonuzdev.pft.features.timer.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sultonuzdev.pft.core.ui.theme.PomodoroAppTheme
import com.sultonuzdev.pft.core.util.TimerType

/**
 * Custom segmented button row for selecting timer type (Pomodoro, Short Break, Long Break)
 */


@Preview()
@Composable
private fun TimerTypeTabsPreview() {
    PomodoroAppTheme {
        TimerTypeTabs(
            selectedTimerType = TimerType.POMODORO,
            onTimerTypeSelected = {}
        )
    }
}

@Composable
fun TimerTypeTabs(
    selectedTimerType: TimerType,
    onTimerTypeSelected: (TimerType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(28.dp)
            )
            .padding(4.dp)
    ) {
        TimerTypeTab(
            text = "Pomodoro",
            selected = selectedTimerType == TimerType.POMODORO,
            color = MaterialTheme.colorScheme.primary,
            onClick = { onTimerTypeSelected(TimerType.POMODORO) },
            modifier = Modifier.weight(1f)
        )

        TimerTypeTab(
            text = "Short Break",
            selected = selectedTimerType == TimerType.SHORT_BREAK,
            color = MaterialTheme.colorScheme.secondary,
            onClick = { onTimerTypeSelected(TimerType.SHORT_BREAK) },
            modifier = Modifier.weight(1f)
        )

        TimerTypeTab(
            text = "Long Break",
            selected = selectedTimerType == TimerType.LONG_BREAK,
            color = MaterialTheme.colorScheme.tertiary,
            onClick = { onTimerTypeSelected(TimerType.LONG_BREAK) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun TimerTypeTab(
    text: String,
    selected: Boolean,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(if (selected) color else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = if (selected)
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
