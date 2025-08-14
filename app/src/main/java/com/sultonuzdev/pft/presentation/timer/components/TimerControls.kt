package com.sultonuzdev.pft.presentation.timer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
        SimpleTimerControls(
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
fun SimpleTimerControls(
    modifier: Modifier = Modifier,
    timerState: TimerState,
    onStartClick: () -> Unit,
    onPauseClick: () -> Unit,
    onResumeClick: () -> Unit,
    onStopClick: () -> Unit,
    onSkipClick: () -> Unit,
    isTablet: Boolean = false
) {
    when (timerState) {
        TimerState.IDLE, TimerState.COMPLETED -> {
            // Only show START button
            Button(
                onClick = onStartClick,
                modifier = modifier.then(
                    if (isTablet) Modifier.width(200.dp) else Modifier.fillMaxWidth()
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = CircleShape
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Start"
                    )
                    Text(
                        text = "Start Focus",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }
            }
        }

        TimerState.RUNNING -> {
            // Show PAUSE and SKIP buttons
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onPauseClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.Pause,
                        contentDescription = "Pause"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Pause")
                }

                FilledTonalButton(
                    onClick = onSkipClick,
                    modifier = Modifier.weight(1f),
                    shape = CircleShape
                ) {
                    Text("Skip")
                }
            }
        }

        TimerState.PAUSED -> {
            // Show RESUME and STOP buttons
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onResumeClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Resume"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Resume")
                }

                OutlinedButton(
                    onClick = onStopClick,
                    modifier = Modifier.weight(1f),
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.Stop,
                        contentDescription = "Stop"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Stop")
                }
            }
        }
    }
}
