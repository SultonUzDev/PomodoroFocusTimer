package com.sultonuzdev.pft.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.sultonuzdev.pft.data.db.AppDatabase
import com.sultonuzdev.pft.data.db.datasource.PomodoroDao
import com.sultonuzdev.pft.data.repository.PomodoroRepositoryImpl
import com.sultonuzdev.pft.data.repository.StatsRepositoryImpl
import com.sultonuzdev.pft.data.repository.TimerSettingsRepositoryImpl
import com.sultonuzdev.pft.domain.repository.PomodoroRepository
import com.sultonuzdev.pft.domain.repository.StatsRepository
import com.sultonuzdev.pft.domain.repository.TimerSettingsRepository
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
    fun providePomodoroDao(database: AppDatabase): PomodoroDao {
        return database.pomodoroDao()
    }

    @Provides
    @Singleton
    fun provideSessionRepository(pomodoroDao: PomodoroDao): PomodoroRepository {
        return PomodoroRepositoryImpl(pomodoroDao)
    }

    @Provides
    @Singleton
    fun provideStatsRepository(
        pomodoroRepository: PomodoroRepository,
    ): StatsRepository = StatsRepositoryImpl(pomodoroRepository)
}