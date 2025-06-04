package com.sultonuzdev.pft.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.sultonuzdev.pft.data.db.AppDatabase
import com.sultonuzdev.pft.core.util.Constants.DATABASE_NAME
import com.sultonuzdev.pft.data.repository.ThemePreferencesRepositoryImpl
import com.sultonuzdev.pft.domain.repository.ThemePreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing application-wide dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val PREFERENCES_NAME = "pomodoro_preferences"

    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile(PREFERENCES_NAME)
        }
    }


    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigrationOnDowngrade()
            .fallbackToDestructiveMigration() // For simplicity in development
            .build()
    }

    @Singleton
    @Provides
    fun provideThemeRepository(dataStore: DataStore<Preferences>): ThemePreferencesRepository =
        ThemePreferencesRepositoryImpl(dataStore)
}