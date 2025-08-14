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
 * Use cases are now provided in SingletonComponent (TimerModule) for broader access
 */
@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {

    // Use cases moved to TimerModule for singleton access by both ViewModels and Services

}
