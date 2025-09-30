package com.sultonuzdev.pft.domain.usecase.settings

import com.sultonuzdev.pft.domain.model.PomodoroTimerSettings
import com.sultonuzdev.pft.domain.repository.PomodoroRepository
import javax.inject.Inject

class UpdatePomodoroSettings @Inject constructor(
    private val repository: PomodoroRepository
) {
    suspend operator fun invoke(pomodoroSettings: PomodoroTimerSettings) {
        repository.updateSettings(pomodoroSettings)
    }

}