package com.sultonuzdev.pft.presentation.timer.screens.meditation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sultonuzdev.pft.core.ui.theme.customColors
import com.sultonuzdev.pft.core.util.TimerState
import com.sultonuzdev.pft.domain.model.DailyStats


/**
 * Meditation tips and encouragement
 */
@Composable
fun MeditationTipsAndEncouragement(
    timerState: TimerState,
    todayStats: DailyStats,
    modifier: Modifier = Modifier
) {
    if (timerState == TimerState.IDLE) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.customColors.meditation.backgroundEnd.copy(alpha = 0.3f)
            ),
            border = BorderStroke(
                1.dp,
                MaterialTheme.customColors.meditation.breatheText.copy(alpha = 0.2f)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = when {
                    todayStats.completedPomodoros == 0 -> "🧘 Begin your meditation journey - Find your center"
                    todayStats.completedPomodoros == 1 -> "✨ Beautiful start! Your spirit is awakening"
                    todayStats.completedPomodoros < 4 -> "🌟 ${todayStats.completedPomodoros} sessions of peace achieved"
                    todayStats.completedPomodoros < 8 -> "🕉️ Remarkable mindfulness practice today"
                    else -> "🌌 You've reached enlightenment - ${todayStats.completedPomodoros} sessions!"
                },
                modifier = Modifier.padding(18.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.customColors.meditation.text.copy(alpha = 0.85f)
            )
        }
    }
}