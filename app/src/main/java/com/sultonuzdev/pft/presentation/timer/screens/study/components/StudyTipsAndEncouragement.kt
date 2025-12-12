package com.sultonuzdev.pft.presentation.timer.screens.study.components

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sultonuzdev.pft.core.ui.theme.customColors
import com.sultonuzdev.pft.core.util.TimerState
import com.sultonuzdev.pft.domain.model.DailyStats

/**
 * Study tips and encouragement
 */
@Composable
 fun StudyTipsAndEncouragement(
    timerState: TimerState,
    todayStats: DailyStats,
    modifier: Modifier = Modifier
) {

    if (timerState == TimerState.IDLE) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.customColors.study.background.copy(alpha = 0.5f)
            ),
            border = BorderStroke(1.dp, MaterialTheme.customColors.study.secondary.copy(alpha = 0.3f)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = when {
                    todayStats.completedPomodoros == 0 -> "📚 Begin your study session - deep focus"
                    todayStats.completedPomodoros == 1 -> "🌟 Excellent start! Your mind is warming up"
                    todayStats.completedPomodoros < 4 -> "🔥 ${todayStats.completedPomodoros} sessions completed! Keep the momentum"
                    todayStats.completedPomodoros < 8 -> "💪 Outstanding study dedication today!"
                    else -> "🚀 You're a study master! ${todayStats.completedPomodoros} sessions!"
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.customColors.study.text.copy(alpha = 0.9f)
            )
        }
    }
}