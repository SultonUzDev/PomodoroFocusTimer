package com.sultonuzdev.pft.domain.usecase


import com.sultonuzdev.pft.domain.repository.TimerSettingsRepository
import com.sultonuzdev.pft.domain.model.TimerSettings
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving timer settings
 */
class GetTimerSettingsUseCase @Inject constructor(
    private val repository: TimerSettingsRepository
) {
    operator fun invoke(): Flow<TimerSettings> {
        return repository.getSettings()
    }
}
