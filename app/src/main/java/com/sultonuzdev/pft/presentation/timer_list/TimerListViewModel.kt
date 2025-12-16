package com.sultonuzdev.pft.presentation.timer_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sultonuzdev.pft.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerListViewModel @Inject constructor(
    private val repository: SettingsRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(TimerListMviContract.TimerListState())
    val uiState: StateFlow<TimerListMviContract.TimerListState> = _uiState.asStateFlow()

    private val _uiSideEffect = Channel<TimerListMviContract.TimerListEffect>()
    val uiSideEffect: Flow<TimerListMviContract.TimerListEffect> = _uiSideEffect.receiveAsFlow()

    fun handleAction(action: TimerListMviContract.TimerListIntent) {
        viewModelScope.launch {
            when (action) {
                is TimerListMviContract.TimerListIntent.SetTimerStyleDefault -> {
                    _uiState.value = _uiState.value.copy(selectedStyle = action.timerStyle)
                    delay(1000L)
                    _uiSideEffect.send(TimerListMviContract.TimerListEffect.ShowMessage("Setting default style"))
                }

                TimerListMviContract.TimerListIntent.LoadTimerList -> {
                    repository.getTimerStyle().collect {
                        _uiState.value = _uiState.value.copy(selectedStyle = it)
                    }
                }

                TimerListMviContract.TimerListIntent.NavigateBack -> {
                    _uiSideEffect.send(TimerListMviContract.TimerListEffect.NavigateBack)
                }

            }
        }
    }
}