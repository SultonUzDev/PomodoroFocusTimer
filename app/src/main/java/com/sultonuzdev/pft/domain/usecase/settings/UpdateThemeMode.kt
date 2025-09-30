package com.sultonuzdev.pft.domain.usecase.settings

import com.sultonuzdev.pft.core.ui.theme.ThemeMode
import com.sultonuzdev.pft.domain.repository.PomodoroRepository
import javax.inject.Inject

class UpdateThemeMode @Inject constructor(
    private val repository: PomodoroRepository
) {
    suspend operator fun invoke(mode: ThemeMode){
        repository.setThemeMode(mode)
    }
}