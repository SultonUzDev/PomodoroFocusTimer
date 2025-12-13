package com.sultonuzdev.pft.presentation.timer.screens.reading


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sultonuzdev.pft.core.ui.theme.PomodoroTheme
import com.sultonuzdev.pft.core.ui.theme.customColors
import com.sultonuzdev.pft.core.util.AppPreview
import com.sultonuzdev.pft.core.util.TimerState
import com.sultonuzdev.pft.domain.model.DailyStats
import com.sultonuzdev.pft.presentation.timer.TimerViewModel
import com.sultonuzdev.pft.presentation.timer.contract.TimerMviContract
import com.sultonuzdev.pft.presentation.timer.screens.reading.components.BookCard
import com.sultonuzdev.pft.presentation.timer.screens.reading.components.ReadingControlButtons
import com.sultonuzdev.pft.presentation.timer.screens.reading.components.ReadingHeader
import com.sultonuzdev.pft.presentation.timer.screens.reading.components.ReadingStats
import com.sultonuzdev.pft.presentation.timer.screens.reading.components.ReadingStatusDisplay
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate


@Composable
fun ReadingTimerScreen(
    viewModel: TimerViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
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

    ReadingTimerScreenContent(
        uiState = uiState,
        onStartClick = { viewModel.processIntent(TimerMviContract.TimerIntent.StartTimer) },
        onPauseClick = { viewModel.processIntent(TimerMviContract.TimerIntent.PauseTimer) },
        onResumeClick = { viewModel.processIntent(TimerMviContract.TimerIntent.ResumeTimer) },
        onStopClick = { viewModel.processIntent(TimerMviContract.TimerIntent.StopTimer) },
        onSkipClick = { viewModel.processIntent(TimerMviContract.TimerIntent.SkipTimer) },
    )
}

/**
 * Reading Timer Screen Content - Book-themed UI
 */
@Composable
fun ReadingTimerScreenContent(
    uiState: TimerMviContract.TimerUiState,
    onStartClick: () -> Unit,
    onPauseClick: () -> Unit,
    onResumeClick: () -> Unit,
    onStopClick: () -> Unit,
    onSkipClick: () -> Unit,
) {
    Column(
        modifier  = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
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
            time = uiState.formattedTime,
            pageStatus = getPageStatus(uiState.timerState),
        )


        // Stats
        ReadingStats(
            completedSession = uiState.todayStats.completedPomodoros,
            timeSpent = uiState.todayStats.totalFocusMinutes,
            sessions = uiState.currentSessionPomodoros,
            currentType = uiState.currentType
        )
        Spacer(modifier = Modifier.height(16.dp))


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
            onStopClick = onStopClick,
            onSkipClick = onSkipClick
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
            onStopClick = {},
            onSkipClick = {},
        )
    }
}
