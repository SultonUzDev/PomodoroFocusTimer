package com.sultonuzdev.pft.di


import com.sultonuzdev.pft.domain.repository.TimerSettingsRepository
import com.sultonuzdev.pft.domain.repository.PomodoroRepository
import com.sultonuzdev.pft.domain.usecase.GetTimerSettingsUseCase
import com.sultonuzdev.pft.domain.usecase.SaveTimerSessionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Hilt module for providing domain layer dependencies
 */
@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {

    @Provides
    @ViewModelScoped
    fun provideGetTimerSettingsUseCase(
        repository: TimerSettingsRepository
    ): GetTimerSettingsUseCase {
        return GetTimerSettingsUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideSaveTimerSessionUseCase(
        repository: PomodoroRepository
    ): SaveTimerSessionUseCase {
        return SaveTimerSessionUseCase(repository)
    }
}
