package com.sultonuzdev.pft.presentation.timer.screens.coding.components

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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.sultonuzdev.pft.core.ui.theme.customColors
import com.sultonuzdev.pft.core.ui.theme.customTypography
import com.sultonuzdev.pft.core.util.TimerState
import com.sultonuzdev.pft.presentation.timer.contract.TimerMviContract


/**
 * Terminal window with dots and content
 */
@Composable
 fun TerminalWindow(
    uiState: TimerMviContract.TimerUiState,
    modifier: Modifier = Modifier
) {

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 500.dp),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.customColors.coding.terminalBg
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            // Terminal header with dots
            TerminalHeader()

            Spacer(modifier = Modifier.height(20.dp))

            // Command line
            CommandLine(
                command = "focus",
                args = "--mode=coding --timer=pomodoro",
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Timer display
            TerminalTimerDisplay(
                time = uiState.formattedTime,
                timerState = uiState.timerState,
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Output/Stats section
            TerminalOutput(
                uiState = uiState,
            )
        }
    }
}

/**
 * Terminal header with colored dots
 */
@Composable
private fun TerminalHeader(
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Red dot
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(MaterialTheme.customColors.coding.dotRed)
        )
        // Yellow dot
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(MaterialTheme.customColors.coding.dotYellow)
        )
        // Green dot
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(MaterialTheme.customColors.coding.dotGreen)
        )
    }
}

/**
 * Command line with prompt
 */
@Composable
private fun CommandLine(
    command: String,
    args: String,
    modifier: Modifier = Modifier
) {


    Row(modifier = modifier) {
        // Prompt
        Text(
            text = "$ ",
            style = MaterialTheme.customTypography.coding.terminal,
            color = MaterialTheme.customColors.coding.prompt
        )
        // Command
        Text(
            text = command,
            style = MaterialTheme.customTypography.coding.terminal,
            color = MaterialTheme.customColors.coding.command
        )
        Text(
            text = " ",
            style = MaterialTheme.customTypography.coding.terminal
        )
        // Args
        Text(
            text = args,
            style = MaterialTheme.customTypography.coding.terminal,
            color = MaterialTheme.customColors.coding.string
        )
    }
}

/**
 * Timer display in terminal style
 */
@Composable
private fun TerminalTimerDisplay(
    time: String,
    timerState: TimerState,

    modifier: Modifier = Modifier
) {


    // Blinking cursor animation
    val infiniteTransition = rememberInfiniteTransition(label = "cursor_blink")
    val cursorAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cursor_alpha"
    )

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Comment
        Text(
            text = "// TIME_REMAINING",
            style = MaterialTheme.customTypography.coding.comment,
            color = MaterialTheme.customColors.coding.comment
        )

        Spacer(modifier = Modifier.height(15.dp))

        // Timer with cursor
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = time,
                style = MaterialTheme.customTypography.coding.timer,
                color = MaterialTheme.customColors.coding.primary
            )

            // Blinking cursor when running
            if (timerState == TimerState.RUNNING) {
                Text(
                    text = "█",
                    style = MaterialTheme.customTypography.coding.timer,
                    color = MaterialTheme.customColors.coding.primary.copy(alpha = cursorAlpha),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Status comment
        Text(
            text = "// ${getStatusComment(timerState)}",
            style = MaterialTheme.customTypography.coding.comment,
            color = MaterialTheme.customColors.coding.comment
        )
    }
}

/**
 * Terminal output with stats
 */
@Composable
private fun TerminalOutput(
    uiState: TimerMviContract.TimerUiState,
    modifier: Modifier = Modifier
) {


    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Output separator
        Text(
            text = ">>> Output:",
            style = MaterialTheme.customTypography.coding.terminal,
            color = MaterialTheme.customColors.coding.prompt
        )

        Spacer(modifier = Modifier.height(15.dp))

        // Stats in code format
        CodeStatLine(
            key = "sessions_completed",
            value = uiState.todayStats.completedPomodoros.toString(),

            )

        CodeStatLine(
            key = "focus_time_minutes",
            value = uiState.todayStats.totalFocusMinutes.toString(),

            )

        CodeStatLine(
            key = "current_session",
            value = "${uiState.currentSessionPomodoros}/4",

            )

        CodeStatLine(
            key = "status",
            value = "\"${getStatusString(uiState.timerState)}\"",
        )
        CodeStatLine(
            key = "current_type",
            value = "\"${uiState.currentType}\"",
        )
    }
}



/**
 * Single stat line in code format
 */
@Composable
private fun CodeStatLine(
    key: String,
    value: String,

    modifier: Modifier = Modifier
) {


    Row(
        modifier = modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Key
        Text(
            text = key,
            style = MaterialTheme.customTypography.coding.statKey,
            color = MaterialTheme.customColors.coding.text
        )
        Text(
            text = ": ",
            style = MaterialTheme.customTypography.coding.statKey,
            color = MaterialTheme.customColors.coding.text
        )
        // Value
        Text(
            text = value,
            style = MaterialTheme.customTypography.coding.statValue,
            color = MaterialTheme.customColors.coding.primary
        )
    }
}

/**
 * Helper functions
 */

private fun getStatusComment(timerState: TimerState): String {
    return when (timerState) {
        TimerState.IDLE -> "Press Start to begin coding session"
        TimerState.RUNNING -> "Stay focused, you're in the zone"
        TimerState.PAUSED -> "Take a breath, then continue"
        TimerState.COMPLETED -> "Session complete! Great work"
    }
}

private fun getStatusString(timerState: TimerState): String {
    return when (timerState) {
        TimerState.IDLE -> "idle"
        TimerState.RUNNING -> "running"
        TimerState.PAUSED -> "paused"
        TimerState.COMPLETED -> "completed"
    }
}
