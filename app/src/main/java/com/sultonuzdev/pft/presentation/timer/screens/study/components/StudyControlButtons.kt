package com.sultonuzdev.pft.presentation.timer.screens.study.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sultonuzdev.pft.core.ui.theme.customColors
import com.sultonuzdev.pft.core.ui.theme.customTypography
import com.sultonuzdev.pft.core.util.TimerState


/**
 * Bottom control buttons - State-aware button layout
 */
@Composable
fun StudyControlButtons(
    timerState: TimerState,
    onResetClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (timerState) {
            TimerState.IDLE, TimerState.COMPLETED -> {
                // Only show START button (larger, centered)
                StudyButton(
                    icon = "▶",
                    label = "START",
                    onClick = onPlayPauseClick
                )
            }

            TimerState.RUNNING -> {
                // Show PAUSE and SKIP buttons
                StudyButton(
                    icon = "⏸",
                    label = "PAUSE",
                    onClick = onPlayPauseClick
                )

                Spacer(modifier = Modifier.width(30.dp))

                StudyButton(
                    icon = "⏭",
                    label = "SKIP",
                    onClick = onSkipClick
                )
            }

            TimerState.PAUSED -> {
                // Show RESUME and STOP buttons
                StudyButton(
                    icon = "▶",
                    label = "RESUME",
                    onClick = onPlayPauseClick
                )

                Spacer(modifier = Modifier.width(30.dp))

                StudyButton(
                    icon = "⏹",
                    label = "FINISH",
                    onClick = onResetClick
                )
            }
        }
    }
}

/**
 * Single circular button with icon and label
 */
@Composable
private fun StudyButton(
    icon: String,
    label: String,
    size: Dp = 90.dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.customColors.study.border,
                    shape = CircleShape
                )
                .background(Color.Transparent)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Text(
                fontSize = if (size > 90.dp) 40.sp else 28.sp,
                text = icon,
                color = MaterialTheme.customColors.study.primary,
                style = MaterialTheme.customTypography.study.buttonIcon
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Label
        Text(
            text = label,
            textAlign = TextAlign.Center,
            color = MaterialTheme.customColors.study.secondary,
            style = MaterialTheme.customTypography.study.buttonLabel
        )
    }
}