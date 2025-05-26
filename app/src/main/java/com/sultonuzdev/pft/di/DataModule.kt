package com.sultonuzdev.pft.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.sultonuzdev.pft.core.data.db.AppDatabase
import com.sultonuzdev.pft.features.settings.data.repository.TimerSettingsRepositoryImpl
import com.sultonuzdev.pft.features.settings.domain.repository.TimerSettingsRepository
import com.sultonuzdev.pft.features.stats.data.datasource.SessionDao
import com.sultonuzdev.pft.features.stats.data.repository.PomodoroRepository
import com.sultonuzdev.pft.features.stats.data.repository.PomodoroRepositoryImpl
import com.sultonuzdev.pft.features.timer.data.repository.StatsRepositoryImpl
import com.sultonuzdev.pft.features.timer.domain.repository.StatsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing data layer dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideTimerSettingsRepository(
        dataStore: DataStore<Preferences>
    ): TimerSettingsRepository {
        return TimerSettingsRepositoryImpl(dataStore)
    }


    @Provides
    @Singleton
    fun provideSessionDao(database: AppDatabase): SessionDao {
        return database.sessionDao()
    }

    @Provides
    @Singleton
    fun provideSessionRepository(sessionDao: SessionDao): PomodoroRepository {
        return PomodoroRepositoryImpl(sessionDao)
    }

    @Provides
    @Singleton
    fun provideStatsRepository(
        pomodoroRepository: PomodoroRepository
    ): StatsRepository = StatsRepositoryImpl(pomodoroRepository)
}