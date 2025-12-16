package com.sultonuzdev.pft.presentation.timer.screens.regular

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sultonuzdev.pft.core.ui.theme.PomodoroTheme
import com.sultonuzdev.pft.core.util.AppPreview
import com.sultonuzdev.pft.core.util.Constants.TIMER_CIRCLE_SIZE_PHONE
import com.sultonuzdev.pft.core.util.TimerState
import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.domain.model.DailyStats
import com.sultonuzdev.pft.domain.model.PomodoroTimerSettings
import com.sultonuzdev.pft.presentation.timer.contract.TimerMviContract
import com.sultonuzdev.pft.presentation.timer.screens.regular.components.CircularTimer
import com.sultonuzdev.pft.presentation.timer.screens.regular.components.SessionSummary
import com.sultonuzdev.pft.presentation.timer.screens.regular.components.SimpleTimerControls
import com.sultonuzdev.pft.presentation.timer.screens.regular.components.TimerTypeTabs
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
    onFinishClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier
) {


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.Top)
    ) {
        // Session Summary
        SessionSummary(
            todayStats = uiState.todayStats,
            modifier = Modifier.fillMaxWidth()
        )

        // Timer Type Tabs
        TimerTypeTabs(
            selectedTimerType = uiState.currentType,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        )


        // Circular timer with enhanced visual feedback
        CircularTimer(
            modifier = Modifier
                .fillMaxWidth(TIMER_CIRCLE_SIZE_PHONE)
                .aspectRatio(1f)
                .padding(16.dp),
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
            onFinishClick = onFinishClick,
            onSkipClick = onSkipClick,
            modifier = Modifier.fillMaxWidth()
        )


        // Focus Mode Indicator
        AnimatedVisibility(
            visible =
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
    PomodoroTheme {
        RegularTimerScreenContent(
            modifier = Modifier
                .padding(vertical = 16.dp),
            uiState = TimerMviContract.TimerUiState(
                currentType = TimerType.SHORT_BREAK,
                timerState = TimerState.IDLE,
                currentTimeMillis = 1500000,
                totalTimeMillis = 1500000,
                progressFraction = 1f,
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
                )
            ),
            onStartClick = {},
            onPauseClick = {},
            onResumeClick = {},
            onFinishClick = {},
            onSkipClick = {}
        )
    }
}


