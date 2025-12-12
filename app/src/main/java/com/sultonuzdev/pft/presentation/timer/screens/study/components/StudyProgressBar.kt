package com.sultonuzdev.pft.presentation.timer.screens.study.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sultonuzdev.pft.core.ui.theme.PomodoroAppTheme
import com.sultonuzdev.pft.core.ui.theme.customColors
import com.sultonuzdev.pft.core.ui.theme.customTypography
import com.sultonuzdev.pft.core.util.AppPreview
import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.core.util.formatAsDuration


/**
 * Top progress bar with 4 stats
 */
@Composable
fun StudyProgressBar(
    completedPomodoros: Int,
    totalFocusMinutes: Int,
    currentSession: Int,
    modifier: Modifier = Modifier,
    currentType: TimerType
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.customColors.study.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)

    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Completed
            StatItem(
                label = "COMPLETED",
                value = completedPomodoros.toString(),

                )

            // Focus Time
            StatItem(
                label = "FOCUS TIME",
                value = totalFocusMinutes.formatAsDuration(),
            )

            // Session
            StatItem(
                label = "SESSION",
                value = "$currentSession/4",

                )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            TimerTypeText(
                text = "Pomodoro",
                isSelected = currentType == TimerType.POMODORO,
            )
            TimerTypeText(
                text = "Short Break",
                isSelected = currentType == TimerType.SHORT_BREAK,
            )
            TimerTypeText(
                text = "Long Break",
                isSelected = currentType == TimerType.LONG_BREAK,


                )
        }
    }
}

@Composable
private fun TimerTypeText(
    text: String,
    isSelected: Boolean,
) {
    Text(
        text = text,
        style = MaterialTheme.customTypography.study.statLabel,
        color = if (isSelected)
            MaterialTheme.customColors.study.primary
        else
            MaterialTheme.customColors.study.secondary
    )
}

/**
 * Single stat item
 */
@Composable
private fun StatItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.customTypography.study.statLabel,
            color = MaterialTheme.customColors.study.secondary,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = value,

            style = MaterialTheme.customTypography.study.statValue,
            color = MaterialTheme.customColors.study.primary
        )
    }
}


@AppPreview
@Composable
private fun StudyProgressBarPreview() {
    PomodoroAppTheme {
        StudyProgressBar(
            completedPomodoros = 3,
            totalFocusMinutes = 75,
            currentSession = 3,
            currentType = TimerType.LONG_BREAK
        )

    }
}