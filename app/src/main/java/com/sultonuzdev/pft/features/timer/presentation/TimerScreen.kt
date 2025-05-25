package com.sultonuzdev.pft.features.timer.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sultonuzdev.pft.core.ui.theme.PomodoroAppTheme
import com.sultonuzdev.pft.core.util.TimerState
import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.features.timer.presentation.components.CircularTimer
import com.sultonuzdev.pft.features.timer.presentation.components.NotificationPermissionHandler
import com.sultonuzdev.pft.features.timer.presentation.components.SessionSummary
import com.sultonuzdev.pft.features.timer.presentation.components.TimerControls
import com.sultonuzdev.pft.features.timer.presentation.components.TimerEffectsHandler
import com.sultonuzdev.pft.features.timer.presentation.components.TimerTopBar
import com.sultonuzdev.pft.features.timer.presentation.components.TimerTypeTabs
import com.sultonuzdev.pft.features.timer.presentation.utils.TimerIntent
import com.sultonuzdev.pft.features.timer.presentation.utils.TimerUiState

/**
 * Root composable for the Timer screen that handles ViewModel integration and navigation
 * Now properly manages service lifecycle with the screen
 */
@Composable
fun TimerScreenRoot(
    viewModel: TimerViewModel = hiltViewModel(),
    navigateToSettings: () -> Unit,
    navigateToStats: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current

    // Request notification permission
    NotificationPermissionHandler()

    // Handle timer effects (sounds, vibrations, messages)
    TimerEffectsHandler(viewModel, snackbarHostState)

    // Handle lifecycle events to manage service connection properly
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    // Screen becomes visible, ensure service connection
                    // ViewModel already handles this in init, but we can add extra safety here
                }
                Lifecycle.Event.ON_STOP -> {
                    // Screen goes to background, but keep service connection
                    // We don't unbind here because we want to keep observing timer state
                }
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    TimerScreen(
        navigateToSettings = navigateToSettings,
        navigateToStats = navigateToStats,
        feedbackDisplay = snackbarHostState,
        uiState = uiState,
        onTypeSelected = { type ->
            viewModel.processIntent(TimerIntent.ChangeTimerType(type))
        },
        onStartClick = { viewModel.processIntent(TimerIntent.StartTimer) },
        onPauseClick = { viewModel.processIntent(TimerIntent.PauseTimer) },
        onResumeClick = { viewModel.processIntent(TimerIntent.ResumeTimer) },
        onStopClick = { viewModel.processIntent(TimerIntent.StopTimer) },
        onSkipClick = { viewModel.processIntent(TimerIntent.SkipTimer) }
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun TimerScreenPreview() {
    PomodoroAppTheme {
        TimerScreen(
            navigateToSettings = {},
            navigateToStats = {},
            feedbackDisplay = remember { SnackbarHostState() },
            uiState = TimerUiState(),
            onTypeSelected = {},
            onStartClick = {},
            onPauseClick = {},
            onResumeClick = {},
            onStopClick = {},
            onSkipClick = {}
        )
    }
}

/**
 * Main timer screen composable
 * Enhanced with better state handling and visual feedback
 */
@Composable
fun TimerScreen(
    navigateToSettings: () -> Unit,
    navigateToStats: () -> Unit,
    feedbackDisplay: SnackbarHostState,
    uiState: TimerUiState,
    onTypeSelected: (TimerType) -> Unit,
    onStartClick: () -> Unit,
    onPauseClick: () -> Unit,
    onResumeClick: () -> Unit,
    onStopClick: () -> Unit,
    onSkipClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TimerTopBar(
                navigateToSettings = navigateToSettings,
                navigateToStats = navigateToStats
            )
        },
        snackbarHost = { SnackbarHost(feedbackDisplay) }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                HorizontalDivider(thickness = 2.dp)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Session summary showing completed pomodoros
                    SessionSummary(
                        completedPomodoros = uiState.completedPomodoros,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Timer type selector - disabled while timer is running to prevent confusion
                    TimerTypeTabs(
                        modifier = Modifier,
                        selectedTimerType = uiState.currentType,
                        onTimerTypeSelected = { type ->
                            if (uiState.timerState == TimerState.IDLE) {
                                onTypeSelected(type)
                            }
                        }
                    )

                    // Enhanced status display
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Timer state indicator
                        Text(
                            text = when (uiState.timerState) {
                                TimerState.IDLE -> "Ready to start"
                                TimerState.RUNNING -> "Timer running"
                                TimerState.PAUSED -> "Timer paused"
                                TimerState.COMPLETED -> "Timer completed!"
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = when (uiState.timerState) {
                                TimerState.RUNNING -> MaterialTheme.colorScheme.primary
                                TimerState.PAUSED -> MaterialTheme.colorScheme.secondary
                                TimerState.COMPLETED -> MaterialTheme.colorScheme.tertiary
                                else -> MaterialTheme.colorScheme.onSurface
                            },
                            textAlign = TextAlign.Center
                        )

                        // Pomodoro count indicator
                        if (uiState.completedPomodoros > 0) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Completed: ${uiState.completedPomodoros} pomodoro${if (uiState.completedPomodoros != 1) "s" else ""}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Circular timer with enhanced visual feedback
                    CircularTimer(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .aspectRatio(1f),
                        progress = uiState.progressFraction,
                        timeText = uiState.formattedTime,
                        progressColor = when (uiState.currentType) {
                            TimerType.POMODORO -> MaterialTheme.colorScheme.primary
                            TimerType.SHORT_BREAK -> MaterialTheme.colorScheme.secondary
                            TimerType.LONG_BREAK -> MaterialTheme.colorScheme.tertiary
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Timer controls with enhanced UX
                    TimerControls(
                        timerState = uiState.timerState,
                        onStartClick = onStartClick,
                        onPauseClick = onPauseClick,
                        onResumeClick = onResumeClick,
                        onStopClick = onStopClick,
                        onSkipClick = onSkipClick
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Focus mode indicator (shown only when in focus mode and running)
                    if (uiState.settings.enableFocusMode &&
                        uiState.timerState == TimerState.RUNNING &&
                        uiState.currentType == TimerType.POMODORO) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🎯 Focus Mode Active",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Stay concentrated on your task!",
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                        }
                    }

                    // Show helpful tips when timer is idle
                    if (uiState.timerState == TimerState.IDLE && uiState.completedPomodoros == 0) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "💡 Tip: Start with a 25-minute focus session",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}