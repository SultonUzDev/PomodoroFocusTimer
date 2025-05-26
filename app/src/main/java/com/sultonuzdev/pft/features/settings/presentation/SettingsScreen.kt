package com.sultonuzdev.pft.features.settings.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sultonuzdev.pft.R
import com.sultonuzdev.pft.core.ui.theme.PomodoroAppTheme
import com.sultonuzdev.pft.core.ui.theme.ThemeMode
import com.sultonuzdev.pft.core.util.Language
import com.sultonuzdev.pft.features.settings.presentation.components.SettingsSection
import com.sultonuzdev.pft.features.settings.presentation.components.SettingsSlider
import com.sultonuzdev.pft.features.settings.presentation.components.SettingsSwitch
import com.sultonuzdev.pft.features.settings.presentation.components.ThemeSelector
import com.sultonuzdev.pft.features.settings.presentation.utils.SettingsEffect
import com.sultonuzdev.pft.features.settings.presentation.utils.SettingsIntent
import com.sultonuzdev.pft.features.settings.presentation.utils.SettingsUiState
import kotlinx.coroutines.flow.collectLatest

/**
 * Settings screen root composable with complete language support
 */
@Composable
fun SettingsScreenRoot(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val uiState by settingsViewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showResetDialog by remember { mutableStateOf(false) }

    // Process UI effects
    LaunchedEffect(settingsViewModel.effect) {
        settingsViewModel.effect.collectLatest { effect ->
            when (effect) {
                is SettingsEffect.ShowMessage -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    SettingsScreen(
        modifier = modifier,
        uiState = uiState,
        onBackClick = onBackClick,
        snackbarHostState = snackbarHostState,
        onTimerDurationChanged = { minutes ->
            settingsViewModel.processIntent(SettingsIntent.UpdatePomodoroMinutes(minutes))
        },
        updateShortBreakMinutes = { minutes ->
            settingsViewModel.processIntent(SettingsIntent.UpdateShortBreakMinutes(minutes))
        },
        updateLongBreakMinutes = { minutes ->
            settingsViewModel.processIntent(SettingsIntent.UpdateLongBreakMinutes(minutes))
        },
        updatePomodorosBeforeLongBreak = { count ->
            settingsViewModel.processIntent(SettingsIntent.UpdatePomodorosBeforeLongBreak(count))
        },
        updateVibrationEnabled = { enabled ->
            settingsViewModel.processIntent(SettingsIntent.UpdateVibrationEnabled(enabled))
        },
        updateSoundEnabled = { enabled ->
            settingsViewModel.processIntent(SettingsIntent.UpdateSoundEnabled(enabled))
        },
        updateFocusModeEnabled = { enabled ->
            settingsViewModel.processIntent(SettingsIntent.UpdateFocusModeEnabled(enabled))
        },
        updateThemeMode = { themeMode ->
            settingsViewModel.processIntent(SettingsIntent.UpdateThemeMode(themeMode))
        },

        onResetDefaults = {
            showResetDialog = true
        }
    )

    // Reset confirmation dialog
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text(stringResource(R.string.reset_settings)) },
            text = { Text(stringResource(R.string.reset_settings_confirmation)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showResetDialog = false
                        settingsViewModel.processIntent(SettingsIntent.ResetToDefaults)
                    }
                ) {
                    Text(stringResource(R.string.reset))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showResetDialog = false }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun SettingsScreenPreview() {
    PomodoroAppTheme {
        SettingsScreen(
            uiState = SettingsUiState(),
            onBackClick = {},
            snackbarHostState = SnackbarHostState(),
            onTimerDurationChanged = {},
            updateShortBreakMinutes = {},
            updateLongBreakMinutes = {},
            updatePomodorosBeforeLongBreak = {},
            updateVibrationEnabled = {},
            updateSoundEnabled = {},
            updateFocusModeEnabled = {},
            updateThemeMode = {},
            onResetDefaults = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    uiState: SettingsUiState,
    onBackClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onTimerDurationChanged: (Int) -> Unit,
    updateShortBreakMinutes: (Int) -> Unit,
    updateLongBreakMinutes: (Int) -> Unit,
    updatePomodorosBeforeLongBreak: (Int) -> Unit,
    updateVibrationEnabled: (Boolean) -> Unit,
    updateSoundEnabled: (Boolean) -> Unit,
    updateFocusModeEnabled: (Boolean) -> Unit,
    updateThemeMode: (ThemeMode) -> Unit,
    onResetDefaults: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                actions = {
                    // Reset button
                    IconButton(onClick = { onResetDefaults() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = stringResource(R.string.reset_to_defaults)
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            HorizontalDivider(thickness = 2.dp)

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // Timer durations section
                SettingsSection(title = stringResource(R.string.timer_durations)) {
                    // Pomodoro duration slider
                    SettingsSlider(
                        value = uiState.settings.pomodoroMinutes,
                        onValueChange = { minutes ->
                            onTimerDurationChanged(minutes)
                        },
                        label = stringResource(R.string.pomodoro),
                        valueRange = 5f..60f,
                        steps = 55,
                        valueLabel = stringResource(
                            R.string.minutes_format,
                            uiState.settings.pomodoroMinutes
                        ),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // Short break duration slider
                    SettingsSlider(
                        value = uiState.settings.shortBreakMinutes,
                        onValueChange = { minutes ->
                            updateShortBreakMinutes(minutes)
                        },
                        label = stringResource(R.string.short_break),
                        valueRange = 1f..30f,
                        steps = 29,
                        valueLabel = stringResource(
                            R.string.minutes_format,
                            uiState.settings.shortBreakMinutes
                        ),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // Long break duration slider
                    SettingsSlider(
                        value = uiState.settings.longBreakMinutes,
                        onValueChange = { minutes ->
                            updateLongBreakMinutes(minutes)
                        },
                        label = stringResource(R.string.long_break),
                        valueRange = 5f..60f,
                        steps = 55,
                        valueLabel = stringResource(
                            R.string.minutes_format,
                            uiState.settings.longBreakMinutes
                        ),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // Pomodoros before long break slider
                    SettingsSlider(
                        value = uiState.settings.pomodorosBeforeLongBreak,
                        onValueChange = { count ->
                            updatePomodorosBeforeLongBreak(count)
                        },
                        label = stringResource(R.string.pomodoros_before_long_break),
                        valueRange = 1f..10f,
                        steps = 9,
                        valueLabel = "${uiState.settings.pomodorosBeforeLongBreak}",
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Notifications section
                SettingsSection(title = stringResource(R.string.notifications)) {
                    // Sound switch
                    SettingsSwitch(
                        checked = uiState.settings.soundEnabled,
                        onCheckedChange = { enabled ->
                            updateSoundEnabled(enabled)
                        },
                        label = stringResource(R.string.sound_enabled),
                        description = stringResource(R.string.sound_description)
                    )

                    // Vibration switch
                    SettingsSwitch(
                        checked = uiState.settings.vibrationEnabled,
                        onCheckedChange = { enabled ->
                            updateVibrationEnabled(enabled)
                        },
                        label = stringResource(R.string.vibration_enabled),
                        description = stringResource(R.string.vibration_description)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Focus mode section
                SettingsSection(title = stringResource(R.string.focus_mode)) {
                    // Focus mode switch
                    SettingsSwitch(
                        checked = uiState.settings.enableFocusMode,
                        onCheckedChange = { enabled ->
                            updateFocusModeEnabled(enabled)
                        },
                        label = stringResource(R.string.focus_mode_enabled),
                        description = stringResource(R.string.focus_mode_description)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Appearance section
                SettingsSection(title = stringResource(R.string.appearance)) {
                    // Theme selector
                    ThemeSelector(
                        selectedThemeMode = uiState.themeMode,
                        onThemeModeSelected = { themeMode ->
                            updateThemeMode(themeMode)
                        }
                    )
                }

//                // Language selector
//                Spacer(modifier = Modifier.height(16.dp))
//
//                SettingsSection(title = stringResource(R.string.language)) {
//                    LanguageSelector(
//                        selectedLanguage = uiState.selectedLanguage,
//                        onLanguageSelected = { language ->
//                            updateLanguage(language)
//                        }
//                    )
//                }


                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}