package com.sultonuzdev.pft.presentation.timer.screens.reading.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.sultonuzdev.pft.core.ui.theme.customColors
import com.sultonuzdev.pft.core.ui.theme.customTypography
import com.sultonuzdev.pft.core.util.TimerState


/**
 * Reading timer status display
 */
@Composable
fun ReadingStatusDisplay(
    timerState: TimerState,
    modifier: Modifier = Modifier
) {
    Text(
        text = when (timerState) {
            TimerState.IDLE -> "Click the book to begin your reading journey"
            TimerState.RUNNING -> "Immerse yourself in the story"
            TimerState.PAUSED -> "Take a moment, bookmark your place"
            TimerState.COMPLETED -> "✨ Wonderful! Great progress made"
        },
        color = when (timerState) {
            TimerState.RUNNING -> MaterialTheme.customColors.reading.title.copy(alpha = 0.9f)
            else -> MaterialTheme.customColors.reading.text.copy(alpha = 0.8f)
        },

        style = MaterialTheme.customTypography.reading.statusText,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}