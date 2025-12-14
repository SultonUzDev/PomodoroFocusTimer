package com.sultonuzdev.pft.presentation.timer.screens.study


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sultonuzdev.pft.core.ui.theme.PomodoroTheme
import com.sultonuzdev.pft.core.ui.theme.customColors
import com.sultonuzdev.pft.core.util.AppPreview
import com.sultonuzdev.pft.core.util.TimerState
import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.domain.model.DailyStats
import com.sultonuzdev.pft.presentation.timer.contract.TimerMviContract
import com.sultonuzdev.pft.presentation.timer.screens.study.components.StudyControlButtons
import com.sultonuzdev.pft.presentation.timer.screens.study.components.StudyFocusModeIndicator
import com.sultonuzdev.pft.presentation.timer.screens.study.components.StudyProgressBar
import com.sultonuzdev.pft.presentation.timer.screens.study.components.StudyTimer
import com.sultonuzdev.pft.presentation.timer.screens.study.components.StudyTipsAndEncouragement
import java.time.LocalDate


/**
 * Study Timer Screen Content - Landscape layout with massive timer
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyTimerScreenContent(
    uiState: TimerMviContract.TimerUiState,
    onStartClick: () -> Unit,
    onPauseClick: () -> Unit,
    onResumeClick: () -> Unit,
    onStopClick: () -> Unit,
    onSkipClick: () -> Unit,
) {

    Column(
        modifier =  Modifier
            .fillMaxSize()
            .background(MaterialTheme.customColors.study.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Progress Bar
        StudyProgressBar(
            completedPomodoros = uiState.todayStats.completedPomodoros,
            totalFocusMinutes = uiState.todayStats.totalFocusMinutes,
            currentSession = uiState.currentSessionPomodoros,
            currentType = uiState.currentType
        )



        HorizontalDivider(
            Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = MaterialTheme.customColors.study.border
        )

        // Center - Massive Timer

        StudyTimer(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f), uiState = uiState
        )

        HorizontalDivider(
            Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = MaterialTheme.customColors.study.border
        )

        // Bottom Controls
        StudyControlButtons(
            timerState = uiState.timerState,
            onResetClick = onStopClick,
            onPlayPauseClick = {
                when (uiState.timerState) {
                    TimerState.RUNNING -> onPauseClick()
                    TimerState.PAUSED -> onResumeClick()
                    else -> onStartClick()
                }
            },
            onSkipClick = onSkipClick,
        )

//             Focus Mode Indicator
        AnimatedVisibility(
            visible = uiState.timerState == TimerState.RUNNING && uiState.currentType == TimerType.POMODORO
        ) {
            StudyFocusModeIndicator(
                primaryColor = MaterialTheme.customColors.study.primary,
                textColor = MaterialTheme.customColors.study.text,
                borderColor = MaterialTheme.customColors.study.border
            )
        }

        // Tips and Encouragement
        StudyTipsAndEncouragement(
            timerState = uiState.timerState,
            todayStats = uiState.todayStats,
        )
    }


}


// Preview
@AppPreview
@Composable
private fun StudyTimerPreview() {
    PomodoroTheme {
        StudyTimerScreenContent(
            uiState = TimerMviContract.TimerUiState(
                timerState = TimerState.IDLE,
                currentType = TimerType.POMODORO,
                formattedTime = "24:35",
                currentSessionPomodoros = 2,
                todayStats = DailyStats(
                    date = LocalDate.now(), completedPomodoros = 3, totalFocusMinutes = 75
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

