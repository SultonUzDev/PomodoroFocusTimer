package com.sultonuzdev.pft.presentation.timer.screens.reading.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sultonuzdev.pft.core.ui.theme.customColors
import com.sultonuzdev.pft.core.ui.theme.customTypography
import com.sultonuzdev.pft.core.util.TimerState


/**
 * State-aware control buttons
 */
@Composable
fun ReadingControlButtons(
    timerState: TimerState,
    onPlayPauseClick: () -> Unit,
    onFinishClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        when (timerState) {
            TimerState.IDLE, TimerState.COMPLETED -> {
                // Only show START button
                ReadingButton(
                    text = "Start Reading",
                    isPrimary = true,
                    onClick = onPlayPauseClick
                )
            }

            TimerState.RUNNING -> {
                // Show PAUSE and SKIP buttons
                ReadingButton(
                    text = "Pause",
                    isPrimary = true,
                    onClick = onPlayPauseClick
                )

                ReadingButton(
                    text = "Skip",
                    isPrimary = false,
                    onClick = onSkipClick
                )
            }

            TimerState.PAUSED -> {
                // Show RESUME and STOP buttons
                ReadingButton(
                    text = "Resume",
                    isPrimary = true,
                    onClick = onPlayPauseClick
                )

                ReadingButton(
                    text = "Finish",
                    isPrimary = false,
                    onClick = onFinishClick
                )
            }
        }
    }
}

/**
 * Single reading button
 */
@Composable
private fun ReadingButton(
    text: String,
    isPrimary: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {


    Button(
        onClick = onClick,
        modifier = modifier
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isPrimary) MaterialTheme.customColors.reading.buttonPrimaryBg else Color.Transparent,
            contentColor = if (isPrimary) MaterialTheme.customColors.reading.buttonPrimaryText else MaterialTheme.customColors.reading.buttonText
        ),
        border = if (!isPrimary) {
            BorderStroke(2.dp, MaterialTheme.customColors.reading.buttonBorder)
        } else null,
        shape = RoundedCornerShape(25.dp)
    ) {
        Text(
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.customTypography.reading.button
        )
    }
}