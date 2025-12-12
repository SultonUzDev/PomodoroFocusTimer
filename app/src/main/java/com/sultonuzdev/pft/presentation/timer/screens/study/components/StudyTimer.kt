package com.sultonuzdev.pft.presentation.timer.screens.study.components

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sultonuzdev.pft.core.ui.theme.customColors
import com.sultonuzdev.pft.core.ui.theme.customTypography
import com.sultonuzdev.pft.core.util.TimerState
import com.sultonuzdev.pft.presentation.timer.contract.TimerMviContract

@Composable
fun StudyTimer(
    modifier: Modifier,
    uiState: TimerMviContract.TimerUiState,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,

        )
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Status Display
            StudyStatusDisplay(
                timerState = uiState.timerState,
            )

            // Timer display
            Text(
                text = uiState.formattedTime,
                style = MaterialTheme.customTypography.study.timer,
                color = MaterialTheme.customColors.study.text,
            )
        }
    }
}

/**
 * Pulsing accent dot animation
 */
@Composable
private fun PulsingDot(
    color: Color,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box(
        modifier = modifier
            .size(12.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(color.copy(alpha = alpha))
    )
}


/**
 * Study timer status display
 */
@Composable
private fun StudyStatusDisplay(
    timerState: TimerState,
    modifier: Modifier = Modifier
) {

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = when (timerState) {
                TimerState.IDLE -> "Ready to study"
                TimerState.RUNNING -> "Stay focused on studying!"
                TimerState.PAUSED -> "Study break - Take a breath"
                TimerState.COMPLETED -> "Study session complete! 🎉"
            },
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = when (timerState) {
                TimerState.RUNNING -> MaterialTheme.customColors.study.primary
                else -> MaterialTheme.customColors.study.text
            },
            textAlign = TextAlign.Center,
            modifier = modifier
        )

        // Pulsing accent dot
        if (timerState == TimerState.RUNNING) {
            PulsingDot(
                color = MaterialTheme.customColors.study.primary,
                modifier = Modifier
            )
        }
    }

}