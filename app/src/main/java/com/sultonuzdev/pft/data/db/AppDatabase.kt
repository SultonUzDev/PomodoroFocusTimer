package com.sultonuzdev.pft.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sultonuzdev.pft.data.db.converter.DateTimeConverter
import com.sultonuzdev.pft.data.db.converter.TimerTypeConverter
import com.sultonuzdev.pft.data.db.datasource.PomodoroDao
import com.sultonuzdev.pft.data.db.entity.PomodoroEntity

/**
 * Room database for the app
 */
@Database(
    entities = [PomodoroEntity::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(DateTimeConverter::class, TimerTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pomodoroDao(): PomodoroDao
}