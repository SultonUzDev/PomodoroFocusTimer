package com.sultonuzdev.pft.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sultonuzdev.pft.core.ui.theme.ThemeMode
import com.sultonuzdev.pft.domain.usecase.PomodoroUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val pomodoroUseCases: PomodoroUseCases,
) : ViewModel() {

    private val _themeModeState = MutableStateFlow(ThemeMode.SYSTEM)
    val themeModeState: StateFlow<ThemeMode> = _themeModeState.asStateFlow()

    init {
        loadTheme()
    }

    fun loadTheme() {
        viewModelScope.launch {
            pomodoroUseCases.getThemeMode().collect {
                _themeModeState.value = it
            }
        }
    }
}