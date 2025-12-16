package com.sultonuzdev.pft.domain.repository

import com.sultonuzdev.pft.domain.model.Pomodoro
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Repository interface for saving and retrieving timer sessions
 */
interface PomodoroRepository {

    suspend fun savePomodoro(pomodoro: Pomodoro)
    fun getAllPomodoros(): Flow<List<Pomodoro>>
    fun getPomodoroByDate(date: LocalDate): Flow<List<Pomodoro>>

    fun getWeeklyStats(startOfTheWeek: LocalDate, endOfTheWeek: LocalDate): Flow<List<Pomodoro>>


}