package com.sultonuzdev.pft.data.repository

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.sultonuzdev.pft.core.enums.TimerStyle
import com.sultonuzdev.pft.core.ui.theme.ThemeMode
import com.sultonuzdev.pft.core.util.Constants.DEFAULT_LONG_BREAK_MINUTES
import com.sultonuzdev.pft.core.util.Constants.DEFAULT_POMODORO_CYCLE_LENGTH
import com.sultonuzdev.pft.core.util.Constants.DEFAULT_POMODORO_MINUTES
import com.sultonuzdev.pft.core.util.Constants.DEFAULT_SHORT_BREAK_MINUTES
import com.sultonuzdev.pft.data.db.dao.PomodoroDao
import com.sultonuzdev.pft.data.mapper.toDomainModel
import com.sultonuzdev.pft.data.mapper.toEntity
import com.sultonuzdev.pft.data.preferences.PreferencesManager
import com.sultonuzdev.pft.domain.model.Pomodoro
import com.sultonuzdev.pft.domain.model.PomodoroTimerSettings
import com.sultonuzdev.pft.domain.repository.PomodoroRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

/**
 * Implementation of SessionRepository that uses Room database for persistence
 */
class PomodoroRepositoryImpl @Inject constructor(
    private val pomodoroDao: PomodoroDao,
) : PomodoroRepository {


    override suspend fun savePomodoro(pomodoro: Pomodoro) {
        pomodoroDao.insertSession(pomodoro.toEntity())
    }

    override fun getAllPomodoros(): Flow<List<Pomodoro>> {
        return pomodoroDao.getAllSessions().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getPomodoroByDate(date: LocalDate): Flow<List<Pomodoro>> {
        return pomodoroDao.getSessionsByDate(date.toString()).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getWeeklyStats(
        startOfTheWeek: LocalDate,
        endOfTheWeek: LocalDate
    ): Flow<List<Pomodoro>> {
        return pomodoroDao.getSessionsByDateRange(
            startOfTheWeek.toString(),
            endOfTheWeek.toString()
        )
            .map { it.toDomainModel() }
    }
}