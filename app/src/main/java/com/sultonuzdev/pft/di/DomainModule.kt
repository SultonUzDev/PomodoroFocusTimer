package com.sultonuzdev.pft.di


import com.sultonuzdev.pft.features.settings.domain.repository.TimerSettingsRepository
import com.sultonuzdev.pft.features.stats.data.repository.SessionRepository
import com.sultonuzdev.pft.features.timer.domain.usecase.GetTimerSettingsUseCase
import com.sultonuzdev.pft.features.timer.domain.usecase.SaveTimerSessionUseCase
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
        repository: SessionRepository
    ): SaveTimerSessionUseCase {
        return SaveTimerSessionUseCase(repository)
    }
}
