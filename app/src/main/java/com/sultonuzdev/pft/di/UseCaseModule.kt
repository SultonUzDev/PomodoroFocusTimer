package com.sultonuzdev.pft.di

import com.sultonuzdev.pft.domain.repository.PomodoroRepository
import com.sultonuzdev.pft.domain.usecase.PomodoroUseCases
import com.sultonuzdev.pft.domain.usecase.pomodoro.AddPomodoro
import com.sultonuzdev.pft.domain.usecase.pomodoro.GetTodayPomodoro
import com.sultonuzdev.pft.domain.usecase.settings.GetPomodoroSettings
import com.sultonuzdev.pft.domain.usecase.settings.GetThemeMode
import com.sultonuzdev.pft.domain.usecase.settings.UpdatePomodoroSettings
import com.sultonuzdev.pft.domain.usecase.settings.UpdateThemeMode
import com.sultonuzdev.pft.domain.usecase.stats.GetCompletedPomodoros
import com.sultonuzdev.pft.domain.usecase.stats.GetDailyStats
import com.sultonuzdev.pft.domain.usecase.stats.GetStartOfWeek
import com.sultonuzdev.pft.domain.usecase.stats.GetTotalFocusTime
import com.sultonuzdev.pft.domain.usecase.stats.GetWeeklyAvgStats
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun providePomodoroUseCases(
        repository: PomodoroRepository
    ): PomodoroUseCases {
        return PomodoroUseCases(
            addPomodoro = AddPomodoro(repository),
            getCompletedPomodoros = GetCompletedPomodoros(repository),
            getTodayPomodoro = GetTodayPomodoro(repository),
            getDailyStats = GetDailyStats(repository),
            getTotalFocusTime = GetTotalFocusTime(repository),
            getWeeklyAvgStats = GetWeeklyAvgStats(repository),
            getStartOfWeek = GetStartOfWeek(),
            getPomodoroSetting = GetPomodoroSettings(repository),
            getThemeMode = GetThemeMode(repository),
            updateThemeMode = UpdateThemeMode(repository),
            updatePomodoroSettings = UpdatePomodoroSettings(repository)

        )
    }
}
