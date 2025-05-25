package com.sultonuzdev.pft.features.timer.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sultonuzdev.pft.core.ui.theme.PomodoroAppTheme

@Preview
@Composable
private fun CircularTimerPreview() {
    PomodoroAppTheme {
        CircularTimer(
            progress = 0.5f,
            timeText = "25:00",
            progressColor = MaterialTheme.colorScheme.primary
        )
    }

}


/**
 * Circular timer component with animated progress bar
 */
@Composable
fun CircularTimer(
    modifier: Modifier = Modifier,
    progress: Float,
    timeText: String,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    progressColor: Color
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        label = "timerProgress"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {


        // Draw the timer circle
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Background circle
            drawCircle(
                color = backgroundColor,
                radius = size.minDimension / 2f,
                style = Stroke(width = 30f, cap = StrokeCap.Round)
            )

            // Progress arc
            val sweepAngle = 360f * animatedProgress
            drawArc(
                color = progressColor,
                startAngle = -90f, // Start from top
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = 30f, cap = StrokeCap.Round)
            )
        }

        // Timer text
        Text(
            text = timeText,
            fontSize = 48.sp,
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center

        )


    }
}