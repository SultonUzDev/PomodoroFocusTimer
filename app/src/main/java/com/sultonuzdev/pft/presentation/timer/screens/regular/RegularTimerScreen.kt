package com.sultonuzdev.pft.presentation.timer.screens.regular

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sultonuzdev.pft.core.ui.theme.PomodoroTheme
import com.sultonuzdev.pft.core.util.AppPreview
import com.sultonuzdev.pft.core.util.Constants.TABLET_CONTENT_WIDTH
import com.sultonuzdev.pft.core.util.Constants.TABLET_CONTROLS_WIDTH
import com.sultonuzdev.pft.core.util.Constants.TIMER_CIRCLE_SIZE_PHONE
import com.sultonuzdev.pft.core.util.Constants.TIMER_CIRCLE_SIZE_TABLET
import com.sultonuzdev.pft.core.util.TimerState
import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.domain.model.DailyStats
import com.sultonuzdev.pft.domain.model.PomodoroTimerSettings
import com.sultonuzdev.pft.presentation.timer.TimerViewModel
import com.sultonuzdev.pft.presentation.timer.contract.TimerMviContract
import com.sultonuzdev.pft.presentation.timer.screens.regular.components.CircularTimer
import com.sultonuzdev.pft.presentation.timer.screens.regular.components.SessionSummary
import com.sultonuzdev.pft.presentation.timer.screens.regular.components.SimpleTimerControls
import com.sultonuzdev.pft.presentation.timer.screens.regular.components.TimerTypeTabs
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate


/**
 * Main timer screen composable with responsive design
 */
@Composable
fun RegularTimerScreenContent(
    uiState: TimerMviContract.TimerUiState,
    onStartClick: () -> Unit,
    onPauseClick: () -> Unit,
    onResumeClick: () -> Unit,
    onStopClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {


        val configuration = LocalConfiguration.current
        val isTablet = configuration.screenWidthDp >= 600
        val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        if (isLandscape || isTablet) {
            TabletTimerLayout(
                uiState = uiState,
                onStartClick = onStartClick,
                onPauseClick = onPauseClick,
                onResumeClick = onResumeClick,
                onStopClick = onStopClick,
                onSkipClick = onSkipClick,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Phone/Portrait layout
            PhoneTimerLayout(
                uiState = uiState,
                onStartClick = onStartClick,
                onPauseClick = onPauseClick,
                onResumeClick = onResumeClick,
                onStopClick = onStopClick,
                onSkipClick = onSkipClick,
                modifier = Modifier.fillMaxSize()

            )
        }
    }

}

@Composable
private fun PhoneTimerLayout(
    uiState: TimerMviContract.TimerUiState,
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
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.Top)
    ) {
        // Session Summary
        SessionSummary(
            todayStats = uiState.todayStats,
            modifier = Modifier.fillMaxWidth()
        )


        Text(
            text = "💡 Complete ${uiState.settings.pomodoroCycleLength} pomodoros to earn a long break!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            modifier = Modifier.padding(8.dp),
            textAlign = TextAlign.Center
        )


        // Timer Type Tabs
        TimerTypeTabs(
            selectedTimerType = uiState.currentType,
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
                .fillMaxWidth(TIMER_CIRCLE_SIZE_PHONE)
                .aspectRatio(1f),
            progress = uiState.progressFraction,
            timeText = uiState.formattedTime,
            progressColor = getTimerColor(uiState.currentType),
        )


        // Timer Controls
        SimpleTimerControls(
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
    uiState: TimerMviContract.TimerUiState,
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
            .padding(16.dp),
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
                    .padding(16.dp)
                    .fillMaxWidth(TIMER_CIRCLE_SIZE_TABLET)
                    .aspectRatio(1f),
                progress = uiState.progressFraction,
                timeText = uiState.formattedTime,
                progressColor = getTimerColor(uiState.currentType),
            )


            // Timer Controls
            SimpleTimerControls(
                timerState = uiState.timerState,
                onStartClick = onStartClick,
                onPauseClick = onPauseClick,
                onResumeClick = onResumeClick,
                onStopClick = onStopClick,
                onSkipClick = onSkipClick,
                modifier = Modifier.fillMaxWidth(TABLET_CONTROLS_WIDTH)
            )
        }

        // Right side - Info and stats
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
        ) {

            SessionSummary(
                todayStats = uiState.todayStats,
                modifier = Modifier.fillMaxWidth(TABLET_CONTENT_WIDTH)
            )

            Text(
                text = "💡 Complete ${uiState.settings.pomodoroCycleLength} pomodoros to earn a long break!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                modifier = Modifier.padding(8.dp),
                textAlign = TextAlign.Center
            )


            // Timer Type Tabs
            TimerTypeTabs(
                selectedTimerType = uiState.currentType,
                modifier = Modifier.fillMaxWidth(TABLET_CONTENT_WIDTH)
            )

            // Status Display
            TimerStatusDisplay(
                timerState = uiState.timerState,
                modifier = Modifier.fillMaxWidth(TABLET_CONTENT_WIDTH)
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
                modifier = Modifier.fillMaxWidth(TABLET_CONTENT_WIDTH)
            )
        }
    }
}


@Composable
private fun TimerStatusDisplay(
    timerState: TimerState,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
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
    todayStats: DailyStats,
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

@AppPreview
@Composable
private fun TimerScreenPreview() {
    PomodoroTheme(darkTheme = false) {
        RegularTimerScreenContent(
            uiState = TimerMviContract.TimerUiState(
                currentType = TimerType.LONG_BREAK,
                timerState = TimerState.IDLE,
                currentTimeMillis = 1500000,
                totalTimeMillis = 1500000,
                progressFraction = 1.0f,
                formattedTime = "25:00",
                currentSessionPomodoros = 2,
                todayStats = DailyStats(
                    completedPomodoros = 8,
                    totalFocusMinutes = 122,
                    date = LocalDate.now()

                ),
                settings = PomodoroTimerSettings(
                    pomodoroMinutes = 25,
                    shortBreakMinutes = 5,
                    longBreakMinutes = 15,
                    pomodoroCycleLength = 4,
                    enableFocusMode = true
                )
            ),
            onStartClick = {},
            onPauseClick = {},
            onResumeClick = {},
            onStopClick = {},
            onSkipClick = {}
        )
    }
}


