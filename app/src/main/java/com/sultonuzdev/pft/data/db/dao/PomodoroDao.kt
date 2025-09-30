package com.sultonuzdev.pft.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sultonuzdev.pft.data.db.entity.PomodoroEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for timer sessions
 */
@Dao
interface PomodoroDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: PomodoroEntity): Long

    @Update
    suspend fun updateSession(session: PomodoroEntity)

    @Query("SELECT * FROM pomodoros ORDER BY startedAt DESC")
    fun getAllSessions(): Flow<List<PomodoroEntity>>

    @Query("SELECT * FROM pomodoros WHERE startedAt BETWEEN :startDate AND :endDate ORDER BY startedAt")
    fun getSessionsByDateRange(
        startDate: String,
        endDate: String
    ): Flow<List<PomodoroEntity>>

    @Query("SELECT * FROM pomodoros WHERE startedAt = :date")
    fun getSessionsByDate(date: String): Flow<List<PomodoroEntity>>

}