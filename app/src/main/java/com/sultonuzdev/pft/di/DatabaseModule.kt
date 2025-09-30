package com.sultonuzdev.pft.di


import android.content.Context
import androidx.room.Room
import com.sultonuzdev.pft.data.db.PomodoroDatabase
import com.sultonuzdev.pft.data.db.dao.PomodoroDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PomodoroDatabase {
        return Room.databaseBuilder(
            context,
            PomodoroDatabase::class.java,
            "pomodoro_db"
        ).build()
    }

    @Provides
    fun providePomodoroDao(database: PomodoroDatabase): PomodoroDao {
        return database.pomodoroDao()
    }
}