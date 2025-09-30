package com.sultonuzdev.pft.domain.usecase.settings

import com.sultonuzdev.pft.core.ui.theme.ThemeMode
import com.sultonuzdev.pft.domain.repository.PomodoroRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThemeMode @Inject constructor(
    private val repository: PomodoroRepository
) {
    operator fun invoke(): Flow<ThemeMode> {
        return repository.getThemeMode()
    }
}