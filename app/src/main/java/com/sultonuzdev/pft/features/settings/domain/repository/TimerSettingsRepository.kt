package com.sultonuzdev.pft.features.settings.domain.repository

import com.sultonuzdev.pft.features.timer.domain.model.TimerSettings
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for accessing and updating timer settings
 */
interface TimerSettingsRepository {
    fun getSettings(): Flow<TimerSettings>
    suspend fun updateSettings(settings: TimerSettings)
}