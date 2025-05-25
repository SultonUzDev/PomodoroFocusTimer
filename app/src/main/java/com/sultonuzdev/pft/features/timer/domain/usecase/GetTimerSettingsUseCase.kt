package com.sultonuzdev.pft.features.timer.domain.usecase


import com.sultonuzdev.pft.features.settings.domain.repository.TimerSettingsRepository
import com.sultonuzdev.pft.features.timer.domain.model.TimerSettings
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
