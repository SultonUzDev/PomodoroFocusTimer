package com.sultonuzdev.pft.domain.repository

import com.sultonuzdev.pft.domain.model.TimerSettings
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for accessing and updating timer settings
 */
interface TimerSettingsRepository {
    fun getSettings(): Flow<TimerSettings>
    suspend fun updateSettings(settings: TimerSettings)
}