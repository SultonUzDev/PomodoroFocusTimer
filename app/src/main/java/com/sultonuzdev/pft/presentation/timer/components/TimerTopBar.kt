package com.sultonuzdev.pft.presentation.timer.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.sultonuzdev.pft.R
import com.sultonuzdev.pft.core.ui.theme.settingsIconColor
import com.sultonuzdev.pft.core.ui.theme.statsIconColor
import com.sultonuzdev.pft.core.ui.theme.timerStyleIconColor

/**
 * Top app bar for the Timer screen
 */


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerTopBar(
    navigateToSettings: () -> Unit,
    navigateToStats: () -> Unit,
    navigateToTimerStyle: () -> Unit,
    containerColor: Color = Color.Unspecified,
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor
        ),
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onBackground),
            )
        },
        actions = {

            // Stats button
            IconButton(onClick = navigateToTimerStyle) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.List,
                    contentDescription = stringResource(R.string.styles),
                    tint = timerStyleIconColor
                )
            }
            // Stats button
            IconButton(onClick = navigateToStats) {
                Icon(
                    imageVector = Icons.Default.Assessment,
                    contentDescription = stringResource(R.string.statistics),
                    tint = statsIconColor
                )
            }

            // Settings button
            IconButton(onClick = navigateToSettings) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(R.string.settings),
                    tint = settingsIconColor
                )
            }
        }
    )
}