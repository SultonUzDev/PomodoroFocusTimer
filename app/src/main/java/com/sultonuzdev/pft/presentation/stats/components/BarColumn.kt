package com.sultonuzdev.pft.presentation.stats.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sultonuzdev.pft.core.ui.theme.PomodoroAppTheme


@Preview
@Composable
private fun BarColumnPreview() {
    PomodoroAppTheme {
        BarColumn(dayOfWeek = "Mon", height = 0.5f, value = 120, isSelected = true)
    }
}

@Composable
fun BarColumn(
    dayOfWeek: String,
    height: Float,
    value: Int,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val animatedHeight by animateFloatAsState(
        targetValue = height,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
    )

    Column(
        modifier = modifier.padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Tooltip style value indicator
        AnimatedVisibility(visible = isSelected) {
            Box(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "$value",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // The actual bar
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(animatedHeight)
                .width(30.dp)
                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = if (isSelected) {
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                            )
                        } else {
                            listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                            )
                        }
                    )
                )
                .padding(top = 4.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            // Optionally add divider lines inside bar
        }

        // Day of week label
        Text(
            text = dayOfWeek,
            style = MaterialTheme.typography.labelMedium,
            color = if (isSelected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}