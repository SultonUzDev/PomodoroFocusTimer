package com.sultonuzdev.pft.presentation.timer.screens.meditation


import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sultonuzdev.pft.R
import com.sultonuzdev.pft.core.ui.theme.PomodoroTheme
import com.sultonuzdev.pft.core.ui.theme.customColors
import com.sultonuzdev.pft.core.ui.theme.customTypography
import com.sultonuzdev.pft.core.util.AppPreview
import com.sultonuzdev.pft.core.util.TimerState
import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.domain.model.DailyStats
import com.sultonuzdev.pft.domain.model.PomodoroTimerSettings
import com.sultonuzdev.pft.presentation.timer.contract.TimerMviContract
import com.sultonuzdev.pft.presentation.timer.screens.meditation.components.MeditationControlButtons
import com.sultonuzdev.pft.presentation.timer.screens.meditation.components.MeditationStats
import com.sultonuzdev.pft.presentation.timer.screens.meditation.components.MeditationTipsAndEncouragement
import java.time.LocalDate


/**
 * Main meditation timer screen with breathing animations
 * Matches the HTML design exactly
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeditationTimerScreenContent(
    uiState: TimerMviContract.TimerUiState,
    onStartClick: () -> Unit,
    onPauseClick: () -> Unit,
    onResumeClick: () -> Unit,
    onFinishClick: () -> Unit,
    onSkipClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        MaterialTheme.customColors.meditation.backgroundStart,
                        MaterialTheme.customColors.meditation.backgroundEnd,
                    )
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top)
    ) {
        // Breathing text at top
        BreathingText(
            timerState = uiState.timerState,
        )


        // Main breathing circle with timer
        BreathingCircleTimer(
            modifier = Modifier.padding(8.dp),
            time = uiState.formattedTime,
            isRunning = uiState.timerState == TimerState.RUNNING
        )


        // Control buttons
        MeditationControlButtons(
            timerState = uiState.timerState,
            onPlayPauseClick = {
                when (uiState.timerState) {
                    TimerState.IDLE -> onStartClick()
                    TimerState.RUNNING -> onPauseClick()
                    TimerState.PAUSED -> onResumeClick()
                    else -> {}
                }
            },
            onFinishClick = onFinishClick,
            onSkipClick = onSkipClick,
            modifier = Modifier
                .padding(vertical = 16.dp)
        )



        MeditationStats(
            completedSessions = uiState.todayStats.completedPomodoros,
            timeSpent = uiState.todayStats.totalFocusMinutes,
            currentSession = uiState.currentSessionPomodoros,
            currentType = uiState.currentType
        )

        MeditationTipsAndEncouragement(
            timerState = uiState.timerState,
            todayStats = uiState.todayStats
        )
    }
}

/**
 * Breathing text that pulses - "Breathe in... Breathe out..."
 */
@Composable
private fun BreathingText(
    timerState: TimerState,
    modifier: Modifier = Modifier
) {
    // Pulse animation for text opacity
    val infiniteTransition = rememberInfiniteTransition(label = "breathe_pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "text_alpha"
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = stringResource(R.string.breathe_in_breathe_out),
            fontSize = 18.sp,
            color = MaterialTheme.customColors.meditation.text.copy(alpha = alpha),
            style = MaterialTheme.customTypography.meditation.breatheText,
            modifier = modifier
        )

        Text(
            text = when (timerState) {
                TimerState.IDLE -> "Ready to meditate"
                TimerState.RUNNING -> "Finding inner peace..."
                TimerState.PAUSED -> "Meditation paused - Breathe gently"
                TimerState.COMPLETED -> "Meditation complete 🧘"
            },
            style = MaterialTheme.customTypography.meditation.breatheText.copy(fontSize = 12.sp),

            color = when (timerState) {
                TimerState.RUNNING -> MaterialTheme.customColors.meditation.primary
                else -> MaterialTheme.customColors.meditation.text.copy(alpha = 0.4f)
            },
            textAlign = TextAlign.Center,
            modifier = modifier
        )
    }


}

/**
 * Main breathing circle with timer display
 * Animates with scale effect like breathing
 */
@Composable
private fun BreathingCircleTimer(
    time: String,
    isRunning: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "breathe_circle")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "circle_scale"
    )

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .scale(if (isRunning) scale else 1f)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        MaterialTheme.customColors.meditation.primary.copy(alpha = 0.2f),
                        Color.Transparent
                    ),
                    radius = 500f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = time,
            style = MaterialTheme.customTypography.meditation.timer,
            color = MaterialTheme.customColors.meditation.primary,
            modifier = Modifier
        )
    }
}


@AppPreview
@Composable
private fun MeditationTimerScreenContentPreview() {
    PomodoroTheme {
        MeditationTimerScreenContent(
            uiState = TimerMviContract.TimerUiState(
                currentType = TimerType.SHORT_BREAK,
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
            onFinishClick = {},
            onSkipClick = {}
        )
    }
}

