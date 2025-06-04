package com.sultonuzdev.pft.presentation.timer.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.sultonuzdev.pft.core.ui.theme.PomodoroAppTheme
import com.sultonuzdev.pft.core.ui.theme.settingsIconColor
import com.sultonuzdev.pft.core.ui.theme.statsIconColor

/**
 * Top app bar for the Timer screen
 */

@Preview
@Composable
private fun TimerAppBarPreview() {
    PomodoroAppTheme {
        TimerTopBar(navigateToSettings = {}, navigateToStats = {})
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerTopBar(
    navigateToSettings: () -> Unit,
    navigateToStats: () -> Unit
) {


    TopAppBar(
        title = {
            Text(
               text =  "Pomodoro Focus Timer",
                style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onBackground),
            )
        },
        actions = {
            // Stats button
            IconButton(onClick = navigateToStats) {
                Icon(
                    imageVector = Icons.Default.Assessment,
                    contentDescription = "Statistics",
                    tint = statsIconColor
                )
            }

            // Settings button
            IconButton(onClick = navigateToSettings) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = settingsIconColor
                )
            }
        }
    )
}