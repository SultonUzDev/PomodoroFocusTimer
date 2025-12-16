package com.sultonuzdev.pft.presentation.timer.screens.regular.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sultonuzdev.pft.R
import com.sultonuzdev.pft.core.util.TimerType

/**
 * Custom segmented button row for selecting timer type (Pomodoro, Short Break, Long Break)
 */


@Composable
fun TimerTypeTabs(
    selectedTimerType: TimerType,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.extraLarge
            )
    ) {
        TimerTypeTab(
            text = stringResource(R.string.pomodoro),
            selected = selectedTimerType == TimerType.POMODORO,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f)
        )

        TimerTypeTab(
            text = stringResource(R.string.short_break),
            selected = selectedTimerType == TimerType.SHORT_BREAK,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.weight(1f)
        )

        TimerTypeTab(
            text = stringResource(R.string.long_break),
            selected = selectedTimerType == TimerType.LONG_BREAK,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun TimerTypeTab(
    text: String,
    selected: Boolean,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .background(if (selected) color else Color.Transparent)
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
