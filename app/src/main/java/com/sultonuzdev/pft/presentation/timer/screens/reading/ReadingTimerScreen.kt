package com.sultonuzdev.pft.presentation.timer.screens.reading


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.sultonuzdev.pft.core.ui.theme.PomodoroTheme
import com.sultonuzdev.pft.core.ui.theme.customColors
import com.sultonuzdev.pft.core.util.AppPreview
import com.sultonuzdev.pft.core.util.Constants
import com.sultonuzdev.pft.core.util.TimerState
import com.sultonuzdev.pft.domain.model.DailyStats
import com.sultonuzdev.pft.presentation.timer.contract.TimerMviContract
import com.sultonuzdev.pft.presentation.timer.screens.reading.components.BookCard
import com.sultonuzdev.pft.presentation.timer.screens.reading.components.ReadingControlButtons
import com.sultonuzdev.pft.presentation.timer.screens.reading.components.ReadingHeader
import com.sultonuzdev.pft.presentation.timer.screens.reading.components.ReadingStats
import com.sultonuzdev.pft.presentation.timer.screens.reading.components.ReadingStatusDisplay
import java.time.LocalDate

/**
 * Reading Timer Screen Content - Book-themed UI
 */
@Composable
fun ReadingTimerScreenContent(
    uiState: TimerMviContract.TimerUiState,
    onStartClick: () -> Unit,
    onPauseClick: () -> Unit,
    onResumeClick: () -> Unit,
    onFinishClick: () -> Unit,
    onSkipClick: () -> Unit,
) {
    Column(
        modifier  = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        MaterialTheme.customColors.reading.backgroundStart,
                        MaterialTheme.customColors.reading.backgroundEnd
                    )
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top)
    ) {
        // Header
        ReadingHeader()


        // Book with timer
        BookCard(
            modifier = Modifier.fillMaxWidth(Constants.TABLET_CONTROLS_WIDTH),
            time = uiState.formattedTime,
            pageStatus = getPageStatus(uiState.timerState)
            ,
        )



        // Control buttons
        ReadingControlButtons(
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
            onSkipClick = onSkipClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Stats
        ReadingStats(
            completedSession = uiState.todayStats.completedPomodoros,
            timeSpent = uiState.todayStats.totalFocusMinutes,
            sessions = uiState.currentSessionPomodoros,
            currentType = uiState.currentType
        )

        // Status text
        ReadingStatusDisplay(
            timerState = uiState.timerState,
        )
    }



}


/**
 * Helper functions
 */

private fun getPageStatus(timerState: TimerState): String {
    return when (timerState) {
        TimerState.IDLE -> "Chapter in Progress"
        TimerState.RUNNING -> "Reading..."
        TimerState.PAUSED -> "Paused"
        TimerState.COMPLETED -> "Chapter Complete!"
    }
}


// Preview
@AppPreview
@Composable
private fun ReadingTimerPreviewDark() {
    PomodoroTheme {

        ReadingTimerScreenContent(
            uiState = TimerMviContract.TimerUiState(
                timerState = TimerState.RUNNING,
                formattedTime = "24:35",
                currentSessionPomodoros = 2,
                todayStats = DailyStats(
                    date = LocalDate.now(), completedPomodoros = 3, totalFocusMinutes = 75
                )
            ),
            onStartClick = {},
            onPauseClick = {},
            onResumeClick = {},
            onFinishClick = {},
            onSkipClick = {},
        )
    }
}
