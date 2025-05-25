package com.sultonuzdev.pft.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sultonuzdev.pft.features.stats.data.converter.DateTimeConverter
import com.sultonuzdev.pft.features.stats.data.converter.TimerTypeConverter
import com.sultonuzdev.pft.features.stats.data.datasource.SessionDao
import com.sultonuzdev.pft.features.stats.data.entity.SessionEntity

/**
 * Room database for the app
 */
@Database(
    entities = [SessionEntity::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(DateTimeConverter::class, TimerTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
}