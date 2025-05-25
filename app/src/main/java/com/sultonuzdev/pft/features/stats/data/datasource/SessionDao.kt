package com.sultonuzdev.pft.features.stats.data.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sultonuzdev.pft.features.stats.data.entity.SessionEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

/**
 * Data Access Object for timer sessions
 */
@Dao
interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: SessionEntity): Long

    @Query("SELECT * FROM sessions ORDER BY startTime DESC")
    fun getAllSessions(): Flow<List<SessionEntity>>

    @Query("SELECT * FROM sessions WHERE date(startTime) = date(:dateTime) ORDER BY startTime DESC")
    fun getSessionsByDate(dateTime: LocalDateTime): Flow<List<SessionEntity>>

    @Query("SELECT * FROM sessions WHERE startTime BETWEEN :startDate AND :endDate ORDER BY startTime")
    fun getSessionsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<SessionEntity>>


}