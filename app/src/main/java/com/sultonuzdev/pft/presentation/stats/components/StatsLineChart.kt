package com.sultonuzdev.pft.presentation.stats.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sultonuzdev.pft.core.ui.theme.PomodoroAppTheme
import java.time.LocalDate


@Preview
@Composable
private fun StatsLineChartPreview() {
    PomodoroAppTheme {
        StatsLineChart(
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
fun StatsLineChart(
    data: List<Pair<LocalDate, Int>>,
    maxValue: Int,
    selectedDay: LocalDate,
    modifier: Modifier = Modifier,
    primaryColor: Color = MaterialTheme.colorScheme.primary,
    secondaryColor: Color = MaterialTheme.colorScheme.primaryContainer
) {
    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 40.dp,
                    top = 20.dp,
                    end = 20.dp,
                    bottom = 40.dp
                )
        ) {

            val height = size.height
            val width = size.width
            val lineWidth = 3.dp.toPx()
            val pointRadius = 6.dp.toPx()

            // Grid
            drawHorizontalLines(height, width, 5)

            if (data.isEmpty()) return@Canvas

            // Calculate positions
            val points = data.mapIndexed { index, (_, value) ->
                val x = index * (width / (data.size - 1))
                val y = height - (value.toFloat() / maxValue * height)
                Offset(x, y)
            }

            // Draw area under the line
            val areaPath = Path().apply {
                moveTo(0f, height)
                lineTo(0f, points.first().y)
                points.forEach { point ->
                    lineTo(point.x, point.y)
                }
                lineTo(width, points.last().y)
                lineTo(width, height)
                close()
            }

            drawPath(
                path = areaPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        primaryColor.copy(alpha = 0.4f),
                        primaryColor.copy(alpha = 0.1f),
                        primaryColor.copy(alpha = 0.0f)
                    )
                )
            )

            // Draw line
            for (i in 0 until points.size - 1) {
                drawLine(
                    color = primaryColor,
                    start = points[i],
                    end = points[i + 1],
                    strokeWidth = lineWidth,
                    cap = StrokeCap.Round
                )
            }

            // Draw points
            points.forEachIndexed { index, point ->
                val isSelected = data[index].first == selectedDay
                val color = if (isSelected) primaryColor else secondaryColor
                val radius = if (isSelected) pointRadius * 1.2f else pointRadius

                drawCircle(
                    color = color,
                    radius = radius,
                    center = point
                )

                if (isSelected) {
                    drawCircle(
                        color = Color.White,
                        radius = radius * 0.5f,
                        center = point
                    )
                }
            }
        }

        // Y-axis labels
        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .width(40.dp)
                    .fillMaxHeight()
                    .padding(top = 20.dp, bottom = 40.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                (0..4).reversed().forEach { i ->
                    val value = (maxValue * i / 4)
                    Text(
                        text = "$value",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
            }
        }

        // X-axis labels
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(start = 40.dp, end = 20.dp, top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                data.forEach { (date, _) ->
                    val dayLabel = date.dayOfWeek.toString().take(3)
                    val isSelected = date == selectedDay
                    Text(
                        text = dayLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSelected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawHorizontalLines(height: Float, width: Float, lines: Int) {
    val gap = height / lines

    repeat(lines + 1) { i ->
        val y = i * gap
        drawLine(
            color = Color.LightGray.copy(alpha = 0.3f),
            start = Offset(0f, y),
            end = Offset(width, y),
            strokeWidth = 1.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f))
        )
    }
}