package com.sultonuzdev.pft.domain.repository

import com.sultonuzdev.pft.domain.model.NewTimerState
import com.sultonuzdev.pft.domain.model.Pomodoro
import kotlinx.coroutines.flow.StateFlow

interface TimerRepository {
    val timerState: StateFlow<NewTimerState>

    suspend fun startTimer()
    suspend fun finishTimer()
    suspend fun pauseTimer()
    suspend fun resumeTimer()
    suspend fun skipTimer()
    suspend fun completedTimer()
    suspend fun savePomodoro(pomodoro: Pomodoro)


}