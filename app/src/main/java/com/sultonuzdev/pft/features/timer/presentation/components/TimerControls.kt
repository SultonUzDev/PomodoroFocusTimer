package com.sultonuzdev.pft.features.timer.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sultonuzdev.pft.core.ui.theme.PomodoroAppTheme
import com.sultonuzdev.pft.core.util.TimerState

@Preview
@Composable
private fun TimerControlsPreview() {
    PomodoroAppTheme {
        TimerControls(
            timerState = TimerState.RUNNING,
            onStartClick = {},
            onPauseClick = {},
            onResumeClick = {},
            onStopClick = {},
            onSkipClick = {})
    }
}

/**
 * Timer control buttons row with play/pause, stop, and skip buttons
 */
@Composable
fun TimerControls(
    timerState: TimerState,
    onStartClick: () -> Unit,
    onPauseClick: () -> Unit,
    onResumeClick: () -> Unit,
    onStopClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier,

        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Start/Pause/Resume button
            when (timerState) {
                TimerState.RUNNING -> {
                    TimerControlButton(
                        onClick = onPauseClick,
                        icon = Icons.Filled.Pause,
                        contentDescription = "Pause Timer",
                        text = "Pause",
                        modifier = Modifier
                            .weight(1f)
                    )
                }

                TimerState.PAUSED -> {
                    TimerControlButton(
                        onClick = onResumeClick,
                        icon = Icons.Filled.PlayArrow,
                        contentDescription = "Resume Timer",
                        text = "Resume",
                        modifier = Modifier
                            .weight(1f)
                    )
                }

                else -> { // IDLE or COMPLETED
                    TimerControlButton(
                        onClick = onStartClick,
                        icon = Icons.Filled.PlayArrow,
                        contentDescription = "Start Timer",
                        text = "Start",
                        modifier = Modifier
                            .weight(1f)
                    )
                }
            }


            // Stop button (visible when timer is running or paused)
            if (timerState == TimerState.RUNNING || timerState == TimerState.PAUSED) {
                TimerControlButton(
                    onClick = onStopClick,
                    icon = Icons.Filled.Stop,
                    contentDescription = "Stop Timer",
                    text = "Stop",
                    modifier = Modifier
                        .weight(1f),
                    containerColor = MaterialTheme.colorScheme.error
                )

            }


        }

        if (timerState == TimerState.RUNNING || timerState == TimerState.PAUSED) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Spacer(Modifier.weight(1f))
                TimerControlButton(
                    onClick = onSkipClick,
                    icon = Icons.Filled.SkipNext,
                    contentDescription = "Skip Timer",
                    text = "Skip",
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.weight(1f)
                )
            }
        }


        // Skip button
    }
}