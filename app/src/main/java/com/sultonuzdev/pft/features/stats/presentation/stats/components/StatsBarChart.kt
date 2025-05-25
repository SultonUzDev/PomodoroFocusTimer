package com.sultonuzdev.pft.features.stats.presentation.stats.components


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sultonuzdev.pft.core.ui.theme.PomodoroAppTheme
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

/**
 * Bar chart for weekly statistics
 */


@Preview
@Composable
private fun StatsBarChartPreview() {

    PomodoroAppTheme {

        StatsBarChart(
            data = listOf(
                LocalDate.now().minusDays(6) to 0,
                LocalDate.now().minusDays(5) to 1,
                LocalDate.now().minusDays(4) to 2,
                LocalDate.now().minusDays(3) to 1,
                LocalDate.now().minusDays(2) to 2,
                LocalDate.now().minusDays(1) to 0,
                LocalDate.now().minusDays(0) to 0,
            ),
            maxValue = 25,
            selectedDay = LocalDate.now().minusDays(5)
        )
    }
}

@Composable
fun StatsBarChart(
    data: List<Pair<LocalDate, Int>>,
    maxValue: Int,
    selectedDay: LocalDate,
    barColor: Color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),

    modifier: Modifier = Modifier
) {
    // Chart implementation with animations, gradients, and rounded corners
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            "Focused time (minutes)",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            // Draw the background grid lines
            Canvas(modifier = Modifier.fillMaxSize()) {
                val height = size.height
                val width = size.width

                // Draw horizontal grid lines
                val gridLineCount = 5
                val gridSpacing = height / gridLineCount

                // Draw grid lines
                for (i in 0..gridLineCount) {
                    val y = height - (i * gridSpacing)
                    drawLine(
                        color = barColor,
                        start = Offset(0f, y),
                        end = Offset(width, y),
                        strokeWidth = 1.dp.toPx()
                    )
                }
            }

            // Draw the bars with animations
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                data.forEach { (date, value) ->
                    val isSelected = date == selectedDay
                    val barHeight = if (maxValue > 0) {
                        (value.toFloat() / maxValue) * 0.9f // 90% of available space for better visuals
                    } else {
                        0f
                    }

                    BarColumn(
                        dayOfWeek = date.dayOfWeek.getDisplayName(
                            TextStyle.SHORT,
                            Locale.getDefault()
                        ),
                        height = barHeight,
                        value = value,
                        isSelected = isSelected,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
