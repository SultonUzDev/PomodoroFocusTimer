package com.sultonuzdev.pft.presentation.timer.screens.regular.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sultonuzdev.pft.R
import com.sultonuzdev.pft.core.util.TimerState


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
    onFinishClick: () -> Unit,
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
                        contentDescription = stringResource(R.string.start)
                    )
                    Text(
                        text = stringResource(R.string.start_focus),
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
                        contentDescription = stringResource(R.string.pause)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.pause))
                }

                FilledTonalButton(
                    onClick = onSkipClick,
                    modifier = Modifier.weight(1f),
                    shape = CircleShape
                ) {
                    Text(stringResource(R.string.skip))
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
                        contentDescription = stringResource(R.string.resume)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.resume))
                }

                OutlinedButton(
                    onClick = onFinishClick,
                    modifier = Modifier.weight(1f),
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.Stop,
                        contentDescription = stringResource(R.string.finish)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.finish))
                }
            }
        }
    }
}
