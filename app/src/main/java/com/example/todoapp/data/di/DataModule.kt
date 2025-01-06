package com.example.todoapp.data.di

import android.content.Context
import androidx.room.Room
import com.example.todoapp.data.RoomTaskLocalDataSource
import com.example.todoapp.data.TaskDao
import com.example.todoapp.data.TodoDatabase
import com.example.todoapp.domain.TaskLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun providesDatabase(
        @ApplicationContext
        context: Context
    ): TodoDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            TodoDatabase::class.java,
            "task_database"
        ).build()
    }

    @Provides
    @Singleton
    fun providesTaskDao(
        database: TodoDatabase
    ): TaskDao = database.taskDao()

    @Provides
    @Singleton
    fun providesTaskLocalDataSource(
        taskDao: TaskDao,
        dispatcher: CoroutineDispatcher
    ): TaskLocalDataSource = RoomTaskLocalDataSource(taskDao)

}