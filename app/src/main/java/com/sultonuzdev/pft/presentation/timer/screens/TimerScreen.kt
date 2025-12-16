package com.sultonuzdev.pft.presentation.timer.screens

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import com.sultonuzdev.pft.core.enums.TimerStyle
import com.sultonuzdev.pft.core.ui.theme.PomodoroTheme
import com.sultonuzdev.pft.core.ui.theme.customColors
import com.sultonuzdev.pft.core.util.AppPreview
import com.sultonuzdev.pft.core.util.TimerState
import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.domain.model.DailyStats
import com.sultonuzdev.pft.domain.model.PomodoroTimerSettings
import com.sultonuzdev.pft.presentation.component.NotificationPermissionHandler
import com.sultonuzdev.pft.presentation.timer.TimerViewModel
import com.sultonuzdev.pft.presentation.timer.components.TimerTopBar
import com.sultonuzdev.pft.presentation.timer.contract.TimerMviContract
import com.sultonuzdev.pft.presentation.timer.screens.coding.CodingTimerScreenContent
import com.sultonuzdev.pft.presentation.timer.screens.meditation.MeditationTimerScreenContent
import com.sultonuzdev.pft.presentation.timer.screens.reading.ReadingTimerScreenContent
import com.sultonuzdev.pft.presentation.timer.screens.regular.RegularTimerScreenContent
import com.sultonuzdev.pft.presentation.timer.screens.study.StudyTimerScreenContent
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate

@Composable
fun TimerScreen(
    viewModel: TimerViewModel = hiltViewModel(),
    navigateToSettings: () -> Unit,
    navigateToStats: () -> Unit,
    navigateToTimerStyle: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Request notification permission
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        NotificationPermissionHandler()
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is TimerMviContract.TimerEffect.ShowMessage -> {
                    snackbarHostState.showSnackbar(effect.message)
                }

                is TimerMviContract.TimerEffect.ShowQuote -> snackbarHostState.showSnackbar(effect.quote)
                TimerMviContract.TimerEffect.NavigateToSettings -> navigateToSettings()
                TimerMviContract.TimerEffect.NavigateToStats -> navigateToStats()
                TimerMviContract.TimerEffect.NavigateToTimerStyle -> navigateToTimerStyle()
            }
        }
    }

    TimerScreenContent(
        uiState = uiState,
        onStartClick = { viewModel.processIntent(TimerMviContract.TimerIntent.StartTimer) },
        onPauseClick = { viewModel.processIntent(TimerMviContract.TimerIntent.PauseTimer) },
        onResumeClick = { viewModel.processIntent(TimerMviContract.TimerIntent.ResumeTimer) },
        onFinishClick = { viewModel.processIntent(TimerMviContract.TimerIntent.FinishTimer) },
        onSkipClick = { viewModel.processIntent(TimerMviContract.TimerIntent.SkipTimer) },
        processIntent = viewModel::processIntent
    )

}

@Composable
fun TimerScreenContent(
    modifier: Modifier = Modifier,
    uiState: TimerMviContract.TimerUiState,
    onStartClick: () -> Unit,
    onPauseClick: () -> Unit,
    onResumeClick: () -> Unit,
    onFinishClick: () -> Unit,
    onSkipClick: () -> Unit,
    processIntent: (TimerMviContract.TimerIntent) -> Unit = {}
) {
    val pagerState = rememberPagerState(
        initialPage = TimerStyle.entries.indexOf(uiState.timerStyle),
        pageCount = { TimerStyle.entries.size }
    )

    // Sync timerStyle when page changes via swipe
    LaunchedEffect(pagerState.currentPage) {
        val currentStyle = TimerStyle.entries[pagerState.currentPage]
        if (currentStyle != uiState.timerStyle) {
            processIntent(TimerMviContract.TimerIntent.SetTimerStyle(currentStyle))
        }
    }

    // Sync pager position when timerStyle changes from other sources
    LaunchedEffect(uiState.timerStyle) {
        val targetPage = TimerStyle.entries.indexOf(uiState.timerStyle)
        if (pagerState.currentPage != targetPage) {
            pagerState.animateScrollToPage(targetPage)
        }
    }


    val backgroundColor = when (uiState.timerStyle) {
        TimerStyle.REGULAR -> listOf(
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.background
        )

        TimerStyle.MEDITATION -> listOf(
            MaterialTheme.customColors.meditation.backgroundStart,
            MaterialTheme.customColors.meditation.backgroundEnd
        )

        TimerStyle.CODING -> listOf(
            MaterialTheme.customColors.coding.background,
            MaterialTheme.customColors.coding.background
        )

        TimerStyle.READING -> listOf(
            MaterialTheme.customColors.reading.backgroundStart,
            MaterialTheme.customColors.reading.backgroundEnd
        )

        TimerStyle.STUDY -> listOf(
            MaterialTheme.customColors.study.background,
            MaterialTheme.customColors.study.background
        )
    }


    val tabTextColor = when (uiState.timerStyle) {
        TimerStyle.REGULAR -> MaterialTheme.colorScheme.onSurface
        TimerStyle.STUDY -> MaterialTheme.customColors.study.text
        TimerStyle.MEDITATION -> MaterialTheme.customColors.meditation.text
        TimerStyle.CODING -> MaterialTheme.customColors.coding.text
        TimerStyle.READING -> MaterialTheme.customColors.reading.text
    }
    val topBarColor = when (uiState.timerStyle) {
        TimerStyle.REGULAR -> MaterialTheme.colorScheme.surface
        TimerStyle.STUDY -> MaterialTheme.colorScheme.surface
        TimerStyle.MEDITATION -> MaterialTheme.customColors.meditation.backgroundStart
        TimerStyle.CODING -> MaterialTheme.customColors.coding.terminalBg.copy(alpha = 0.7f)
        TimerStyle.READING -> MaterialTheme.customColors.reading.backgroundStart
    }





    Column(
        modifier = modifier
            .background(brush = Brush.radialGradient(backgroundColor))
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top)
    ) {
        TimerTopBar(
            navigateToStats = {
                processIntent(TimerMviContract.TimerIntent.NavigateToStats)
            },
            navigateToSettings = {
                processIntent(TimerMviContract.TimerIntent.NavigateToSettings)
            },
            navigateToTimerStyle = {
                processIntent(TimerMviContract.TimerIntent.NavigateToTimerStyle)
            },
            containerColor = topBarColor
        )

        Text(
            text = uiState.timerStyle.title,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .clickable {
                    processIntent(TimerMviContract.TimerIntent.SetTimerStyle(uiState.timerStyle))
                }
                .padding(vertical = 2.dp),
            softWrap = false,
            color = tabTextColor.copy(alpha = 0.5f),
        )

        HorizontalPager(state = pagerState) { page ->
            val currentStyle = TimerStyle.entries[page]
            when (currentStyle) {
                TimerStyle.REGULAR -> {
                    RegularTimerScreenContent(
                        uiState = uiState,
                        onStartClick = onStartClick,
                        onPauseClick = onPauseClick,
                        onResumeClick = onResumeClick,
                        onFinishClick = onFinishClick,
                        onSkipClick = onSkipClick,
                    )

                }

                TimerStyle.READING -> {
                    ReadingTimerScreenContent(
                        uiState = uiState,
                        onStartClick = onStartClick,
                        onPauseClick = onPauseClick,
                        onResumeClick = onResumeClick,
                        onFinishClick = onFinishClick,
                        onSkipClick = onSkipClick,
                    )
                }

                TimerStyle.STUDY -> {
                    StudyTimerScreenContent(
                        uiState = uiState,
                        onStartClick = onStartClick,
                        onPauseClick = onPauseClick,
                        onResumeClick = onResumeClick,
                        onFinishClick = onFinishClick,
                        onSkipClick = onSkipClick,
                    )
                }

                TimerStyle.MEDITATION -> {
                    MeditationTimerScreenContent(
                        uiState = uiState,
                        onStartClick = onStartClick,
                        onPauseClick = onPauseClick,
                        onResumeClick = onResumeClick,
                        onFinishClick = onFinishClick,
                        onSkipClick = onSkipClick,
                    )
                }

                TimerStyle.CODING -> {
                    CodingTimerScreenContent(
                        uiState = uiState,
                        onStartClick = onStartClick,
                        onPauseClick = onPauseClick,
                        onResumeClick = onResumeClick,
                        onFinishClick = onFinishClick,
                        onSkipClick = onSkipClick,
                    )
                }
            }

        }


    }


}

@AppPreview
@Composable
fun TimerScreenContentPreview() {
    PomodoroTheme {
        TimerScreenContent(
            uiState = TimerMviContract.TimerUiState(
                currentType = TimerType.LONG_BREAK,
                timerState = TimerState.IDLE,
                currentTimeMillis = 1500000,
                totalTimeMillis = 1500000,
                progressFraction = 1.0f,
                formattedTime = "25:00",
                currentSessionPomodoros = 2,
                timerStyle = TimerStyle.STUDY,
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