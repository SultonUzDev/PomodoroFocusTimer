package com.sultonuzdev.pft.features.timer.presentation

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
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
import com.sultonuzdev.pft.features.timer.domain.model.TimerSettings
import com.sultonuzdev.pft.features.timer.domain.model.TodayStats
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
                }

                Lifecycle.Event.ON_STOP -> {
                    // Screen goes to background, but keep service connection
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

@Preview(
    name = "Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Preview(
    name = "Tablet",
    device = "spec:width=1280dp,height=800dp,dpi=240",
    showBackground = true
)
@Composable
private fun TimerScreenPreview() {
    PomodoroAppTheme {
        TimerScreen(
            navigateToSettings = {},
            navigateToStats = {},
            feedbackDisplay = remember { SnackbarHostState() },
            uiState = TimerUiState(
                currentType = TimerType.POMODORO,
                timerState = TimerState.IDLE,
                currentTimeMillis = 1500000,
                totalTimeMillis = 1500000,
                progressFraction = 1.0f,
                formattedTime = "25:00",
                currentSessionPomodoros = 2,
                todayStats = TodayStats(
                    completedPomodoros = 8,
                    completedSessions = 2,
                    focusTimeMinutes = 122
                ),
                settings = TimerSettings(
                    pomodoroMinutes = 25,
                    shortBreakMinutes = 5,
                    longBreakMinutes = 15,
                    pomodorosBeforeLongBreak = 4,
                    enableFocusMode = true
                )
            ),
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
 * Main timer screen composable with responsive design
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
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(
        topBar = {
            TimerTopBar(
                navigateToSettings = navigateToSettings,
                navigateToStats = navigateToStats
            )
        },
        snackbarHost = { SnackbarHost(feedbackDisplay) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    when (uiState.currentType) {
                        TimerType.POMODORO -> Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.background,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                            )
                        )

                        TimerType.SHORT_BREAK -> Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.background,
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f)
                            )
                        )

                        TimerType.LONG_BREAK -> Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.background,
                                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.05f)
                            )
                        )
                    }
                )
        ) {
            if (isTablet || isLandscape) {
                // Tablet/Landscape layout
                TabletTimerLayout(
                    uiState = uiState,
                    onTypeSelected = onTypeSelected,
                    onStartClick = onStartClick,
                    onPauseClick = onPauseClick,
                    onResumeClick = onResumeClick,
                    onStopClick = onStopClick,
                    onSkipClick = onSkipClick,
                    modifier = Modifier.padding(paddingValues)
                )
            } else {
                // Phone/Portrait layout
                PhoneTimerLayout(
                    uiState = uiState,
                    onTypeSelected = onTypeSelected,
                    onStartClick = onStartClick,
                    onPauseClick = onPauseClick,
                    onResumeClick = onResumeClick,
                    onStopClick = onStopClick,
                    onSkipClick = onSkipClick,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun PhoneTimerLayout(
    uiState: TimerUiState,
    onTypeSelected: (TimerType) -> Unit,
    onStartClick: () -> Unit,
    onPauseClick: () -> Unit,
    onResumeClick: () -> Unit,
    onStopClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.Top)
    ) {
        // Session Summary
        SessionSummary(
            currentSessionPomodoros = uiState.currentSessionPomodoros,
            pomodorosBeforeLongBreak = uiState.settings.pomodorosBeforeLongBreak,
            todayPomodoros = uiState.todayStats.completedPomodoros,
            todaySessions = uiState.todayStats.completedSessions,
            todayFocusTimeMinutes = uiState.todayStats.focusTimeMinutes,
            modifier = Modifier.fillMaxWidth()
        )


        // Timer Type Tabs
        TimerTypeTabs(
            selectedTimerType = uiState.currentType,
            onTimerTypeSelected = { type ->
                if (uiState.timerState == TimerState.IDLE) {
                    onTypeSelected(type)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )


        // Status Display
        TimerStatusDisplay(
            timerState = uiState.timerState,
            modifier = Modifier.fillMaxWidth()
        )


        // Circular timer with enhanced visual feedback
        CircularTimer(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .aspectRatio(1f),
            progress = uiState.progressFraction,
            timeText = uiState.formattedTime,
            progressColor = getTimerColor(uiState.currentType),
        )


        // Timer Controls
        TimerControls(
            timerState = uiState.timerState,
            onStartClick = onStartClick,
            onPauseClick = onPauseClick,
            onResumeClick = onResumeClick,
            onStopClick = onStopClick,
            onSkipClick = onSkipClick,
            modifier = Modifier.fillMaxWidth()
        )


        // Focus Mode Indicator
        AnimatedVisibility(
            visible = uiState.settings.enableFocusMode &&
                    uiState.timerState == TimerState.RUNNING &&
                    uiState.currentType == TimerType.POMODORO
        ) {
            FocusModeIndicator()
        }

        // Tips and Encouragement
        TipsAndEncouragement(
            timerState = uiState.timerState,
            todayStats = uiState.todayStats,
            modifier = Modifier.padding(vertical = 16.dp)
        )
    }
}

@Composable
private fun TabletTimerLayout(
    uiState: TimerUiState,
    onTypeSelected: (TimerType) -> Unit,
    onStartClick: () -> Unit,
    onPauseClick: () -> Unit,
    onResumeClick: () -> Unit,
    onStopClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.Start),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left side - Timer and controls
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {


            // Circular timer with enhanced visual feedback
            CircularTimer(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .aspectRatio(1f),
                progress = uiState.progressFraction,
                timeText = uiState.formattedTime,
                progressColor = getTimerColor(uiState.currentType),
            )


            // Timer Controls
            TimerControls(
                timerState = uiState.timerState,
                onStartClick = onStartClick,
                onPauseClick = onPauseClick,
                onResumeClick = onResumeClick,
                onStopClick = onStopClick,
                onSkipClick = onSkipClick,
                modifier = Modifier.fillMaxWidth(0.8f)
            )
        }

        // Right side - Info and stats
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
        ) {
            // Session Summary
            SessionSummary(
                currentSessionPomodoros = uiState.currentSessionPomodoros,
                pomodorosBeforeLongBreak = uiState.settings.pomodorosBeforeLongBreak,
                todayPomodoros = uiState.todayStats.completedPomodoros,
                todaySessions = uiState.todayStats.completedSessions,
                todayFocusTimeMinutes = uiState.todayStats.focusTimeMinutes,
                modifier = Modifier
                    .fillMaxWidth(0.9f)

            )

            // Timer Type Tabs
            TimerTypeTabs(
                selectedTimerType = uiState.currentType,
                onTimerTypeSelected = { type ->
                    if (uiState.timerState == TimerState.IDLE) {
                        onTypeSelected(type)
                    }
                },
                modifier = Modifier.fillMaxWidth(0.9f)
            )

            // Status Display
            TimerStatusDisplay(
                timerState = uiState.timerState,
                modifier = Modifier.fillMaxWidth(0.9f)
            )

            // Focus Mode Indicator
            AnimatedVisibility(
                visible = uiState.settings.enableFocusMode &&
                        uiState.timerState == TimerState.RUNNING &&
                        uiState.currentType == TimerType.POMODORO
            ) {
                FocusModeIndicator()
            }

            // Tips and Encouragement
            TipsAndEncouragement(
                timerState = uiState.timerState,
                todayStats = uiState.todayStats,
                modifier = Modifier.fillMaxWidth(0.9f)
            )
        }
    }
}

@Composable
private fun TimerStatusDisplay(
    timerState: TimerState,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Timer state
            Text(
                text = when (timerState) {
                    TimerState.IDLE -> "Ready to focus"
                    TimerState.RUNNING -> "Stay focused!"
                    TimerState.PAUSED -> "Paused - Take a breath"
                    TimerState.COMPLETED -> "Well done! 🎉"
                },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = when (timerState) {
                    TimerState.RUNNING -> MaterialTheme.colorScheme.primary
                    TimerState.PAUSED -> MaterialTheme.colorScheme.secondary
                    TimerState.COMPLETED -> MaterialTheme.colorScheme.tertiary
                    else -> MaterialTheme.colorScheme.onSurface
                }
            )

        }
    }
}

@Composable
private fun FocusModeIndicator(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🎯",
                fontSize = 20.sp
            )
            Column {
                Text(
                    text = "Focus Mode Active",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Stay concentrated!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun TipsAndEncouragement(
    timerState: TimerState,
    todayStats: TodayStats,
    modifier: Modifier = Modifier
) {
    if (timerState == TimerState.IDLE) {
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Text(
                text = when {
                    todayStats.completedPomodoros == 0 -> "💡 Start with a 25-minute focus session"
                    todayStats.completedPomodoros == 1 -> "🌟 Great start! Keep going"
                    todayStats.completedPomodoros < 4 -> "🔥 ${todayStats.completedPomodoros} down! You're doing great"
                    todayStats.completedPomodoros < 8 -> "💪 Impressive focus today!"
                    else -> "🚀 You're a productivity master!"
                },
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun getTimerColor(timerType: TimerType): Color {
    return when (timerType) {
        TimerType.POMODORO -> MaterialTheme.colorScheme.primary
        TimerType.SHORT_BREAK -> MaterialTheme.colorScheme.secondary
        TimerType.LONG_BREAK -> MaterialTheme.colorScheme.tertiary
    }
}

// Add this for AnimatedVisibility
@Composable
private fun AnimatedVisibility(
    visible: Boolean,
    content: @Composable () -> Unit
) {
    if (visible) {
        content()
    }
}