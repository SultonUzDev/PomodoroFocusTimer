package com.sultonuzdev.pft.presentation.timer.screens.coding


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sultonuzdev.pft.core.ui.theme.PomodoroAppTheme
import com.sultonuzdev.pft.core.ui.theme.customColors
import com.sultonuzdev.pft.core.ui.theme.customTypography
import com.sultonuzdev.pft.core.util.AppPreview
import com.sultonuzdev.pft.core.util.TimerState
import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.domain.model.DailyStats
import com.sultonuzdev.pft.presentation.timer.TimerViewModel
import com.sultonuzdev.pft.presentation.timer.contract.TimerMviContract
import com.sultonuzdev.pft.presentation.timer.screens.coding.components.CodingFocusModeIndicator
import com.sultonuzdev.pft.presentation.timer.screens.coding.components.CodingTipsAndEncouragement
import com.sultonuzdev.pft.presentation.timer.screens.coding.components.TerminalWindow
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate

@Composable
fun CodingTimerScreen(
    viewModel: TimerViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is TimerMviContract.TimerEffect.ShowMessage -> {
                    snackbarHostState.showSnackbar(effect.message)
                }

                is TimerMviContract.TimerEffect.ShowQuote -> snackbarHostState.showSnackbar(effect.quote)
            }
        }
    }

    CodingTimerScreenContent(
        uiState = uiState,
        onStartClick = { viewModel.processIntent(TimerMviContract.TimerIntent.StartTimer) },
        onPauseClick = { viewModel.processIntent(TimerMviContract.TimerIntent.PauseTimer) },
        onResumeClick = { viewModel.processIntent(TimerMviContract.TimerIntent.ResumeTimer) },
        onStopClick = { viewModel.processIntent(TimerMviContract.TimerIntent.StopTimer) },
        onSkipClick = { viewModel.processIntent(TimerMviContract.TimerIntent.SkipTimer) },
    )
}

/**
 * Coding Timer Screen Content - Terminal/VS Code style
 */
@Composable
fun CodingTimerScreenContent(
    uiState: TimerMviContract.TimerUiState,
    onStartClick: () -> Unit,
    onPauseClick: () -> Unit,
    onResumeClick: () -> Unit,
    onStopClick: () -> Unit,
    onSkipClick: () -> Unit,
) {



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.customColors.coding.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top)
    ) {
        // Terminal window
        TerminalWindow(
            uiState = uiState,
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Control buttons
        CodingControlButtons(
            timerState = uiState.timerState,
            onResetClick = onStopClick,
            onPlayPauseClick = {
                when (uiState.timerState) {
                    TimerState.IDLE -> onStartClick()
                    TimerState.RUNNING -> onPauseClick()
                    TimerState.PAUSED -> onResumeClick()
                    else -> {}
                }
            },
            onSkipClick = onSkipClick
        )

        Spacer(modifier = Modifier.height(16.dp))

//             Focus Mode Indicator
        AnimatedVisibility(
            visible = uiState.timerState == TimerState.RUNNING && uiState.currentType == TimerType.POMODORO
        ) {
            CodingFocusModeIndicator()
        }

        // Tips and Encouragement
        CodingTipsAndEncouragement(
            timerState = uiState.timerState,
            todayStats = uiState.todayStats,

            )
    }
}


/**
 * State-aware control buttons in terminal style
 */
@Composable
private fun CodingControlButtons(
    timerState: TimerState,
    onResetClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        when (timerState) {
            TimerState.IDLE, TimerState.COMPLETED -> {
                // Only show START button
                CodingButton(
                    text = "Start Coding",
                    enabled = true,
                    onClick = onPlayPauseClick
                )
            }

            TimerState.RUNNING -> {
                // Show PAUSE and SKIP buttons
                CodingButton(
                    text = "Pause",
                    enabled = true,
                    onClick = onPlayPauseClick
                )

                CodingButton(
                    text = "Skip",
                    enabled = true,
                    onClick = onSkipClick
                )
            }

            TimerState.PAUSED -> {
                // Show RESUME and STOP buttons
                CodingButton(
                    text = "Resume",
                    enabled = true,
                    onClick = onPlayPauseClick
                )

                CodingButton(
                    text = "Stop",
                    enabled = true,
                    onClick = onResetClick
                )
            }
        }
    }
}

/**
 * Single terminal-style button
 */
@Composable
private fun CodingButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {


    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.customColors.coding.primary,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.customColors.coding.text.copy(alpha = 0.3f)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (enabled) {
                MaterialTheme.customColors.coding.primary
            } else {
                MaterialTheme.customColors.coding.text.copy(alpha = 0.3f)
            }
        ),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.customTypography.coding.button
        )
    }
}


// Preview
@AppPreview
@Composable
private fun CodingTimerPreview() {
    PomodoroAppTheme {
        CodingTimerScreenContent(
            uiState = TimerMviContract.TimerUiState(
                timerState = TimerState.IDLE,
                formattedTime = "24:35",
                currentSessionPomodoros = 2,
                todayStats = DailyStats(
                    date = LocalDate.now(),
                    completedPomodoros = 3,
                    totalFocusMinutes = 75
                )
            ),
            onStartClick = {},
            onPauseClick = {},
            onResumeClick = {},
            onStopClick = {},
            onSkipClick = {},
        )

    }
}


