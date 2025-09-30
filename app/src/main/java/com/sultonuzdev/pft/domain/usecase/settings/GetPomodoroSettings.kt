package com.sultonuzdev.pft.domain.usecase.settings

import com.sultonuzdev.pft.domain.model.PomodoroTimerSettings
import com.sultonuzdev.pft.domain.repository.PomodoroRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving timer settings
 */
class GetPomodoroSettings @Inject constructor(
    private val repository: PomodoroRepository
) {
    operator fun invoke(): Flow<PomodoroTimerSettings> {
        return repository.getSettings()
    }
}