package com.sultonuzdev.pft.presentation.timer.screens.coding.components

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.sultonuzdev.pft.core.ui.theme.PomodoroTheme
import com.sultonuzdev.pft.core.ui.theme.customColors
import com.sultonuzdev.pft.core.ui.theme.customTypography
import com.sultonuzdev.pft.core.util.AppPreview
import com.sultonuzdev.pft.core.util.TimerState
import com.sultonuzdev.pft.core.util.TimerType
import com.sultonuzdev.pft.domain.model.DailyStats
import com.sultonuzdev.pft.domain.model.PomodoroTimerSettings
import com.sultonuzdev.pft.presentation.timer.contract.TimerMviContract
import java.time.LocalDate


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
            .heightIn(min = 400.dp),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.customColors.coding.terminalBg
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            // Terminal header with dots
            TerminalHeader()

            Spacer(modifier = Modifier.height(20.dp))

            // Command line
            CommandLine(
                args = getCommand(uiState.currentType, uiState.settings),
                modifier = Modifier.fillMaxWidth()
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
    modifier: Modifier = Modifier,
    args: String
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
            text = "dev@focus:",
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


    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top)
    ) {
        // Comment
        Text(
            text = "// TIME_REMAINING",
            style = MaterialTheme.customTypography.coding.comment,
            color = MaterialTheme.customColors.coding.comment
        )


        // Timer with cursor

        Text(
            text = time,
            style = MaterialTheme.customTypography.coding.timer,
            color = MaterialTheme.customColors.coding.primary
        )


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

private fun getCommand(
    timerType: TimerType,
    settings: PomodoroTimerSettings
): String {
    return when (timerType) {
        TimerType.POMODORO -> "$ pomodoro work --mode=coding --time=${settings.pomodoroMinutes}:00"
        TimerType.SHORT_BREAK -> "$ pomodoro break --short --time=${settings.shortBreakMinutes}:00"
        TimerType.LONG_BREAK -> "$ pomodoro break --long --time=${settings.longBreakMinutes}:00"
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

@AppPreview
@Composable
private fun TerminalViewPreview() {
    PomodoroTheme {
        TerminalWindow(
            uiState = TimerMviContract.TimerUiState(
                timerState = TimerState.IDLE,
                formattedTime = "24:35",
                currentSessionPomodoros = 2,
                todayStats = DailyStats(
                    date = LocalDate.now(),
                    completedPomodoros = 3,
                    totalFocusMinutes = 75
                )
            ),
        )
    }
}