package com.sultonuzdev.pft.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sultonuzdev.pft.core.ui.theme.ThemeMode
import com.sultonuzdev.pft.domain.model.PomodoroTimerSettings
import com.sultonuzdev.pft.domain.usecase.PomodoroUseCases
import com.sultonuzdev.pft.presentation.settings.utils.SettingsEffect
import com.sultonuzdev.pft.presentation.settings.utils.SettingsIntent
import com.sultonuzdev.pft.presentation.settings.utils.SettingsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Settings screen implementing MVI pattern
 * Complete working implementation with proper language support
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val pomodoroUseCases: PomodoroUseCases,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<SettingsEffect>()
    val effect: SharedFlow<SettingsEffect> = _effect.asSharedFlow()

    init {
        loadSettings()
        loadThemeMode()
    }


    private fun loadThemeMode() {
        viewModelScope.launch {
            try {
                pomodoroUseCases.getThemeMode().collectLatest { themeMode ->
                    _uiState.update { it.copy(themeMode = themeMode) }
                }
            } catch (e: Exception) {
                // Handle error silently
            }
        }
    }

    private fun loadSettings() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                pomodoroUseCases.getPomodoroSetting().collectLatest { settings ->
                    _uiState.update {
                        it.copy(
                            settings = settings,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to load settings: ${e.message}"
                    )
                }
            }
        }
    }

    fun processIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.UpdatePomodoroMinutes -> updateSettings {
                it.copy(pomodoroMinutes = intent.minutes.coerceIn(5, 60))
            }

            is SettingsIntent.UpdateShortBreakMinutes -> updateSettings {
                it.copy(shortBreakMinutes = intent.minutes.coerceIn(1, 30))
            }

            is SettingsIntent.UpdateLongBreakMinutes -> updateSettings {
                it.copy(longBreakMinutes = intent.minutes.coerceIn(5, 60))
            }

            is SettingsIntent.UpdatePomodorosBeforeLongBreak -> updateSettings {
                it.copy(pomodoroCycleLength = intent.count.coerceIn(1, 10))
            }

            is SettingsIntent.UpdateVibrationEnabled -> updateSettings {
                it.copy(vibrationEnabled = intent.enabled)
            }

            is SettingsIntent.UpdateSoundEnabled -> updateSettings {
                it.copy(soundEnabled = intent.enabled)
            }

            is SettingsIntent.UpdateFocusModeEnabled -> updateSettings {
                it.copy(enableFocusMode = intent.enabled)
            }

            is SettingsIntent.UpdateThemeMode -> {
                viewModelScope.launch {
                    try {
                        pomodoroUseCases.updateThemeMode(intent.themeMode)
                        _uiState.update { it.copy(themeMode = intent.themeMode) }
                        _effect.emit(SettingsEffect.ShowMessage("Theme updated"))
                    } catch (e: Exception) {
                        _effect.emit(SettingsEffect.ShowMessage("Failed to update theme"))
                    }
                }
            }


            is SettingsIntent.ResetToDefaults -> {
                viewModelScope.launch {
                    try {
                        // Reset to default settings
                        val defaultSettings = PomodoroTimerSettings()
                        pomodoroUseCases.updatePomodoroSettings(defaultSettings)

                        // Reset theme to system default
                        pomodoroUseCases.updateThemeMode(ThemeMode.SYSTEM)


                        // Update UI state
                        _uiState.update {
                            it.copy(
                                settings = defaultSettings,
                                themeMode = ThemeMode.SYSTEM,
                            )
                        }

                        // Show message
                        _effect.emit(SettingsEffect.ShowMessage("Settings reset to defaults"))
                    } catch (e: Exception) {
                        _effect.emit(SettingsEffect.ShowMessage("Failed to reset settings"))
                    }
                }
            }
        }
    }

    private fun updateSettings(update: (PomodoroTimerSettings) -> PomodoroTimerSettings) {
        viewModelScope.launch {
            try {
                val currentSettings = _uiState.value.settings
                val newSettings = update(currentSettings)

                // Update local state immediately for responsive UI
                _uiState.update { it.copy(settings = newSettings) }

                // Persist settings
                pomodoroUseCases.updatePomodoroSettings(newSettings)

                // Show success message
                _effect.emit(SettingsEffect.ShowMessage("Settings updated"))
            } catch (e: Exception) {
                // Show error
                _effect.emit(SettingsEffect.ShowMessage("Failed to update settings: ${e.message}"))
            }
        }
    }
}