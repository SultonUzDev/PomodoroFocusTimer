package com.sultonuzdev.pft.di

import android.content.Context
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
}