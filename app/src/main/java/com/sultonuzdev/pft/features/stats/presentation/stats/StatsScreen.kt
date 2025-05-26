package com.sultonuzdev.pft.features.stats.presentation.stats

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sultonuzdev.pft.core.ui.theme.PomodoroAppTheme
import com.sultonuzdev.pft.features.stats.presentation.stats.components.ChartType
import com.sultonuzdev.pft.features.stats.presentation.stats.components.ChartTypeToggle
import com.sultonuzdev.pft.features.stats.presentation.stats.components.DateSelector
import com.sultonuzdev.pft.features.stats.presentation.stats.components.StatsBarChart
import com.sultonuzdev.pft.features.stats.presentation.stats.components.StatsCard
import com.sultonuzdev.pft.features.stats.presentation.stats.components.StatsLineChart
import com.sultonuzdev.pft.features.stats.presentation.stats.components.StatsSection
import com.sultonuzdev.pft.features.stats.presentation.stats.components.StatsSectionHeader
import com.sultonuzdev.pft.features.stats.presentation.stats.utils.StatsEffect
import com.sultonuzdev.pft.features.stats.presentation.stats.utils.StatsIntent
import com.sultonuzdev.pft.features.stats.presentation.stats.utils.StatsUiState
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun StatsScreenRoot(
    viewModel: StatsViewModel = hiltViewModel(),
    onBackClick: () -> Unit,

) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }


    // Generate dates for the date selector
    val weekDates = remember(uiState.weekStartDate) {
        (0..6).map { uiState.weekStartDate.plusDays(it.toLong()) }
    }

    // Process UI effects
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is StatsEffect.ShowMessage -> {
                    snackbarHostState.showSnackbar(effect.message)
                }


                is StatsEffect.ShareStats -> {
                    // In a real app, we would use the Intent system to share this text
                    snackbarHostState.showSnackbar("Sharing stats")
                }
            }
        }
    }
    StatsScreen(
        modifier = Modifier,
        uiState = uiState,
        onBackClick = onBackClick,
        snackbarHostState = snackbarHostState,
        onRefreshStats = { viewModel.processIntent(StatsIntent.RefreshStats) },
        onDaySelect = { date -> viewModel.processIntent(StatsIntent.SelectDate(date)) },
        weekDates = weekDates,
        onNextWeek = { viewModel.processIntent(StatsIntent.NavigateToNextWeek) },
        onPreviousWeek = { viewModel.processIntent(StatsIntent.NavigateToPreviousWeek) },
    )


}


/**
 * Stats screen composable
 */

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun StatsScreenPreview() {

    PomodoroAppTheme {
        StatsScreen(
            modifier = Modifier,
            uiState = StatsUiState(),
            onBackClick = {},
            snackbarHostState = SnackbarHostState(),
            onRefreshStats = {},
            onDaySelect = {},
            weekDates = emptyList(),
            onNextWeek = {},
            onPreviousWeek = {},

            )
    }

}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun StatsScreen(
    modifier: Modifier = Modifier,
    uiState: StatsUiState,
    onBackClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onRefreshStats: () -> Unit,
    onDaySelect: (LocalDate) -> Unit,
    weekDates: List<LocalDate>,
    onNextWeek: () -> Unit,
    onPreviousWeek: () -> Unit,
) {

    var chartType by remember { mutableStateOf(ChartType.BAR) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Statistics",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .clip(CircleShape)
                            .padding(4.dp)

                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // Refresh button
                    IconButton(
                        onClick = { onRefreshStats() },
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .clip(CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh Statistics"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.shadow(elevation = 4.dp)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        Column(modifier = modifier.padding(paddingValues)) {
            HorizontalDivider(thickness = 2.dp)
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(64.dp),
                        strokeWidth = 6.dp,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {

                        DateSelector(
                            dates = weekDates,
                            selectedDate = uiState.selectedDate,
                            onDateSelected = onDaySelect,
                            onPreviousWeek = onPreviousWeek,
                            onNextWeek = onNextWeek,
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .wrapContentHeight()
                        )


                        // Daily stats section
                        StatsSectionHeader(title = "Daily Focus")

                        // Selected date display
                        val formattedDate = uiState.selectedDate.format(
                            DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")
                        )

                        Text(
                            text = formattedDate,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateContentSize(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            StatsCard(
                                title = "Pomodoros",
                                value = uiState.dailyStats?.completedPomodoros?.toString() ?: "0",
                                subtitle = "Completed",
                                icon = Icons.Default.Timer,
                                iconTint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.weight(1f)
                            )

                            StatsCard(
                                title = "Focus Time",
                                value = uiState.dailyStats?.totalFocusMinutes?.toString() ?: "0",
                                subtitle = "Minutes",
                                icon = Icons.Default.AccessTime,
                                iconTint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // Weekly focus section with chart type toggle
                        StatsSection(
                            title = "Weekly Focus",
                            actions = {
                                ChartTypeToggle(
                                    selectedType = chartType,
                                    onTypeSelected = { chartType = it }
                                )
                            },
                            content = {
                                AnimatedContent(
                                    targetState = chartType,
                                    transitionSpec = {
                                        fadeIn() + slideInHorizontally() togetherWith
                                                fadeOut() + slideOutHorizontally()
                                    }
                                ) { currentChartType ->
                                    when (currentChartType) {
                                        ChartType.BAR -> {
                                            StatsBarChart(
                                                data = uiState.weeklyFocusData,
                                                maxValue = uiState.maxWeeklyFocusMinutes,
                                                selectedDay = uiState.selectedDate,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(300.dp)
                                                    .padding(8.dp)
                                            )
                                        }

                                        ChartType.LINE -> {
                                            StatsLineChart(
                                                data = uiState.weeklyFocusData,
                                                maxValue = uiState.maxWeeklyFocusMinutes,
                                                selectedDay = uiState.selectedDate,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(300.dp)
                                                    .padding(8.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        )


                        // Overall stats section
                        StatsSectionHeader(title = "Overall Statistics")

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateContentSize(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            StatsCard(
                                title = "Pomodoros",
                                value = uiState.totalCompletedPomodoros.toString(),
                                subtitle = "Completed",
                                icon = Icons.Default.CheckCircle,
                                modifier = Modifier.weight(1f)
                            )

                            StatsCard(
                                title = "Focus",
                                value = uiState.totalFocusMinutes.toString(),
                                subtitle = "Minutes",
                                icon = Icons.Default.Timelapse,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        StatsCard(
                            title = "Daily Average",
                            value = uiState.averageDailyFocusMinutes.toString(),
                            subtitle = "Minutes/Day",
                            icon = Icons.AutoMirrored.Filled.TrendingUp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }


                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}







