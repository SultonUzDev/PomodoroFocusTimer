package com.sultonuzdev.pft.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.sultonuzdev.pft.data.db.dao.PomodoroDao
import com.sultonuzdev.pft.data.repository.PomodoroRepositoryImpl
import com.sultonuzdev.pft.domain.repository.PomodoroRepository
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
        dataStore: DataStore<Preferences>
    ): PomodoroRepository {
        return PomodoroRepositoryImpl(dao, dataStore)
    }
}