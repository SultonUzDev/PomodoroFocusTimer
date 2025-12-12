package com.sultonuzdev.pft.domain.usecase

import com.sultonuzdev.pft.domain.usecase.pomodoro.AddPomodoro
import com.sultonuzdev.pft.domain.usecase.pomodoro.GetTodayPomodoro
import com.sultonuzdev.pft.domain.usecase.stats.GetCompletedPomodoros
import com.sultonuzdev.pft.domain.usecase.stats.GetDailyStats
import com.sultonuzdev.pft.domain.usecase.stats.GetTotalFocusTime
import com.sultonuzdev.pft.domain.usecase.stats.GetWeeklyAvgStats

data class PomodoroUseCases(
    val addPomodoro: AddPomodoro,
    val getTodayPomodoro: GetTodayPomodoro,
    val getCompletedPomodoros: GetCompletedPomodoros,
    val getDailyStats: GetDailyStats,
    val getTotalFocusTime: GetTotalFocusTime,
    val getWeeklyAvgStats: GetWeeklyAvgStats,
)