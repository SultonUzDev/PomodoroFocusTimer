package com.sultonuzdev.pft.di

import com.sultonuzdev.pft.data.db.dao.PomodoroDao
import com.sultonuzdev.pft.data.preferences.PreferencesManager
import com.sultonuzdev.pft.data.repository.PomodoroRepositoryImpl
import com.sultonuzdev.pft.data.repository.SettingsRepositoryImpl
import com.sultonuzdev.pft.domain.repository.PomodoroRepository
import com.sultonuzdev.pft.domain.repository.SettingsRepository
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
object RepositoryModule {

    @Provides
    @Singleton
    fun providePomodoroRepository(
        dao: PomodoroDao,
    ): PomodoroRepository {
        return PomodoroRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(
        preferencesManager: PreferencesManager
    ): SettingsRepository = SettingsRepositoryImpl(preferencesManager)



}