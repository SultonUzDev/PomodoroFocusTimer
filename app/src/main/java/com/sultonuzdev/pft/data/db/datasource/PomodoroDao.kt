package com.sultonuzdev.pft.data.db.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sultonuzdev.pft.data.db.entity.PomodoroEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

/**
 * Data Access Object for timer sessions
 */
@Dao
interface PomodoroDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: PomodoroEntity): Long

    @Query("SELECT * FROM pomodoros ORDER BY startTime DESC")
    fun getAllSessions(): Flow<List<PomodoroEntity>>

    @Query("SELECT * FROM pomodoros WHERE date(startTime) = date(:dateTime) ORDER BY startTime DESC")
    fun getSessionsByDate(dateTime: LocalDateTime): Flow<List<PomodoroEntity>>

    @Query("SELECT * FROM pomodoros WHERE startTime BETWEEN :startDate AND :endDate ORDER BY startTime")
    fun getSessionsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<PomodoroEntity>>


}