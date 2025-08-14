package com.sultonuzdev.pft.di

import android.content.Context
import com.sultonuzdev.pft.domain.repository.PomodoroRepository
import com.sultonuzdev.pft.domain.repository.TimerSettingsRepository
import com.sultonuzdev.pft.domain.usecase.GetTimerSettingsUseCase
import com.sultonuzdev.pft.domain.usecase.SaveTimerSessionUseCase
import com.sultonuzdev.pft.presentation.service.TimerServiceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TimerModule {

    @Provides
    @Singleton
    fun provideTimerServiceManager(
        @ApplicationContext context: Context
    ): TimerServiceManager {
        return TimerServiceManager(context)
    }
    
    @Provides
    @Singleton
    fun provideGetTimerSettingsUseCaseSingleton(
        repository: TimerSettingsRepository
    ): GetTimerSettingsUseCase {
        return GetTimerSettingsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSaveTimerSessionUseCaseSingleton(
        repository: PomodoroRepository
    ): SaveTimerSessionUseCase {
        return SaveTimerSessionUseCase(repository)
    }
}