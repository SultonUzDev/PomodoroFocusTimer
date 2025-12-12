package com.sultonuzdev.pft.presentation.timer.screens.coding.components

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
import com.sultonuzdev.pft.core.ui.theme.customTypography
import com.sultonuzdev.pft.core.util.TimerState
import com.sultonuzdev.pft.domain.model.DailyStats

/**
 * Coding tips and encouragement
 */
@Composable
 fun CodingTipsAndEncouragement(
    timerState: TimerState,
    todayStats: DailyStats,
    modifier: Modifier = Modifier
) {

    if (timerState == TimerState.IDLE) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.customColors.coding.terminalBg
            ),
            border = BorderStroke(1.dp, MaterialTheme.customColors.coding.comment.copy(alpha = 0.3f)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = when {
                    todayStats.completedPomodoros == 0 -> "// TODO: Start your first coding session - Write beautiful code"
                    todayStats.completedPomodoros == 1 -> "// SUCCESS: First session complete! Keep the flow going"
                    todayStats.completedPomodoros < 4 -> "// INFO: ${todayStats.completedPomodoros} sessions completed - You're building momentum"
                    todayStats.completedPomodoros < 8 -> "// IMPRESSIVE: Solid coding dedication - Keep shipping features"
                    else -> "// LEGENDARY: ${todayStats.completedPomodoros} sessions! You're a code warrior!"
                },
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.customTypography.coding.comment,
                textAlign = TextAlign.Center,
                color = MaterialTheme.customColors.coding.text
            )
        }
    }
}