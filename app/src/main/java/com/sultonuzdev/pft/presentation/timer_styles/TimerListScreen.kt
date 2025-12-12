package com.sultonuzdev.pft.presentation.timer_styles

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sultonuzdev.pft.R
import com.sultonuzdev.pft.core.enums.TimerStyle
import com.sultonuzdev.pft.core.ui.theme.PomodoroAppTheme
import com.sultonuzdev.pft.core.ui.theme.customColors
import com.sultonuzdev.pft.core.ui.theme.customTypography
import com.sultonuzdev.pft.domain.model.TimerOption
import com.sultonuzdev.pft.presentation.timer_styles.components.TimerTopBar
import kotlinx.coroutines.flow.collectLatest


@Composable
fun TimerListScreen(
    navigateToSettings: () -> Unit,
    navigateToStats: () -> Unit,
    navigateToTimer: (TimerStyle) -> Unit,
    viewModel: TimerListViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Request notification permission
//    NotificationPermissionHandler()

    LaunchedEffect(Unit) {
        viewModel.uiSideEffect.collectLatest { effect ->
            when (effect) {
                is TimerListMviContract.TimerListEffect.NavigateToTimer -> {
                    navigateToTimer(effect.timerStyle)
                }

                TimerListMviContract.TimerListEffect.NavigateToSettings -> navigateToSettings()
                TimerListMviContract.TimerListEffect.NavigateToStats -> navigateToStats()
            }
        }
    }




    TimerListScreenContent(
        handleAction = viewModel::handleAction,
        uiState = uiState,
        modifier = Modifier.fillMaxSize(),
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerListScreenContent(
    handleAction: (TimerListMviContract.TimerListIntent) -> Unit,
    uiState: TimerListMviContract.TimerListState,
    modifier: Modifier = Modifier,
) {


    Scaffold(
        modifier = modifier,
        topBar = {
            TimerTopBar(
                navigateToSettings = {
                    handleAction(
                        TimerListMviContract.TimerListIntent.NavigateToSettings
                    )
                },
                navigateToStats = {
                    handleAction(
                        TimerListMviContract.TimerListIntent.NavigateToStats
                    )
                }
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {


                // Header
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            text = "Select a timer theme that matches your task and mood",
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }

                // Timer Cards
                items(uiState.timerList) { option ->
                    TimerCard(
                        option = option,
                        onClick = {
                            handleAction(
                                TimerListMviContract.TimerListIntent.NavigateToTimer(
                                    option.style
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TimerCard(
    option: TimerOption,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val backgroundColor = when (option.style) {
        TimerStyle.REGULAR -> listOf(
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.surface
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
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                brush = Brush.linearGradient(
                    colors = backgroundColor
                ),
                shape = RoundedCornerShape(16.dp),
            ),
    ) {


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clickable(onClick = onClick)

        ) {
            // Preview Section
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center

            ) {
                when (option.style) {
                    TimerStyle.REGULAR -> RegularPreview()
                    TimerStyle.READING -> ReadingPreview()
                    TimerStyle.CODING -> CodingPreview()
                    TimerStyle.MEDITATION -> MeditationPreview()
                    TimerStyle.STUDY -> StudyPreview()
                }
            }

            // Content Section
            TimerPreviewInfoSection(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(8.dp),
                option = option
            )
        }
    }
}

@Composable
fun TimerPreviewInfoSection(
    modifier: Modifier,
    option: TimerOption,

    ) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        // Title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(2.dp)
        ) {
            Text(
                text = option.icon,
                fontSize = 14.sp,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = option.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Description
        Text(
            text = option.description,
            color = Color.Gray,
            style = MaterialTheme.typography.labelMedium
        )

        // Feature Tags
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            option.features.forEach { feature ->
                Surface(
                    shape = RoundedCornerShape(15.dp),
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = feature,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                    )
                }
            }
        }
    }
}


// Preview Components for each theme

@Composable
fun RegularPreview() {

    val circleColor = MaterialTheme.colorScheme.primary
    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        // Draw the timer circle
        Canvas(modifier = Modifier.fillMaxSize(0.9f)) {
            // Background circle
            drawCircle(
                color = circleColor,
                radius = size.minDimension / 2f,
                style = Stroke(width = 20f, cap = StrokeCap.Round)
            )
        }
        Text(
            text = "25:00",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge
        )

    }
}

@Composable
fun StudyPreview() {


    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.preview_25),
            fontSize = 42.sp,
            style = MaterialTheme.customTypography.study.timer,
            color = MaterialTheme.customColors.study.text,
        )

        // Pulsing accent dot
        val infiniteTransition = rememberInfiniteTransition()
        val scale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.2f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(20.dp)
                .size(12.dp)
                .scale(scale)
                .background(MaterialTheme.customColors.study.primary, CircleShape)
        )
    }
}

@Composable
fun ReadingPreview() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .shadow(8.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(12.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.customColors.reading.bookStart,
                            MaterialTheme.customColors.reading.bookEnd
                        )
                    )
                )
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            // Book spine line
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp)
                    .width(2.dp)
                    .fillMaxHeight(0.7f)
                    .background(Color.Black.copy(alpha = 0.2f))
            )
            Text(
                text = stringResource(R.string.preview_25),
                style = MaterialTheme.customTypography.reading.timer.copy(fontSize = 24.sp),
                color = MaterialTheme.customColors.reading.timerText
            )

        }
        // Book spine line

    }
}

@Composable
fun MeditationPreview() {

    val textColor = MaterialTheme.customColors.meditation.text

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val infiniteTransition = rememberInfiniteTransition()
        val scale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.08f,
            animationSpec = infiniteRepeatable(
                animation = tween(4000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )


        Box(
            modifier = Modifier
                .size(120.dp)
                .scale(scale)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            textColor.copy(alpha = 0.3f), Color.Transparent
                        )
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.preview_25),
                fontSize = 32.sp,
                color = textColor,
                style = MaterialTheme.customTypography.meditation.timer
            )
        }
    }
}


@Composable
fun CodingPreview() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.customColors.coding.terminalBg
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                // Terminal dots
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(bottom = 10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(MaterialTheme.customColors.coding.dotRed, CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(MaterialTheme.customColors.coding.dotYellow, CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(MaterialTheme.customColors.coding.dotGreen, CircleShape)
                    )
                }

                // Terminal line
                Row {
                    Text(
                        text = "$",
                        fontSize = 10.sp,
                        color = MaterialTheme.customColors.coding.string,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = " focus --mode=coding",
                        fontSize = 10.sp,
                        color = MaterialTheme.customColors.coding.command,
                        style = MaterialTheme.customTypography.coding.terminal
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Timer
                Text(
                    text = stringResource(R.string.preview_25),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.customTypography.coding.timer.copy(fontSize = 25.sp),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}


@Preview(device = "spec:width=411dp,height=1000dp")
@Composable
fun TimerListScreenContentPreview() {
    PomodoroAppTheme(darkTheme = true) {
        TimerListScreenContent(
            handleAction = {},
            uiState = TimerListMviContract.TimerListState()
        )
    }
}

@Preview
@Composable
fun TimerListScreenContentLightPreview() {
    PomodoroAppTheme(darkTheme = false) {
        TimerListScreenContent(
            handleAction = {},
            uiState = TimerListMviContract.TimerListState()
        )
    }
}






