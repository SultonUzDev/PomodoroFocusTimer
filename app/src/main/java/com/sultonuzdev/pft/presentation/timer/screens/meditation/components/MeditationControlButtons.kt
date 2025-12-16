package com.sultonuzdev.pft.presentation.timer.screens.meditation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.sultonuzdev.pft.core.ui.theme.customColors
import com.sultonuzdev.pft.core.util.TimerState

/**
 * State-aware circular control buttons
 */
@Composable
fun MeditationControlButtons(
    timerState: TimerState,
    onPlayPauseClick: () -> Unit,
    onFinishClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        when (timerState) {
            TimerState.IDLE, TimerState.COMPLETED -> {
                // Only show START button (larger)
                MeditationButton(
                    icon = Icons.Default.PlayArrow,
                    onClick = onPlayPauseClick,
                )
            }

            TimerState.RUNNING -> {
                // Show PAUSE and SKIP buttons
                MeditationButton(
                    icon = Icons.Default.Pause,
                    onClick = onPlayPauseClick
                )

                MeditationButton(
                    icon = Icons.Default.SkipNext,
                    onClick = onSkipClick
                )
            }

            TimerState.PAUSED -> {
                // Show RESUME and STOP buttons
                MeditationButton(
                    icon = Icons.Default.PlayArrow,
                    onClick = onPlayPauseClick
                )

                MeditationButton(
                    icon = Icons.Default.Done,
                    onClick = onFinishClick
                )
            }
        }
    }
}

/**
 * Single circular button with icon
 */
@Composable
private fun MeditationButton(
    icon: ImageVector,
    onClick: () -> Unit,
    contentDescription: String? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(64.dp)
            .clip(CircleShape)
            .border(
                width = 1.dp,

                color = MaterialTheme.customColors.meditation.buttonBorder,
                shape = CircleShape
            )
            .background(Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            modifier = Modifier.size(24.dp),
            contentDescription = contentDescription,
            tint = MaterialTheme.customColors.meditation.primary
        )
    }
}