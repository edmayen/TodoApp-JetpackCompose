package com.example.todoapp.data

import android.content.Context
import com.example.todoapp.domain.TaskLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object DataSourceFactory {
    fun createDataSource(
        context: Context,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): TaskLocalDataSource {
        val database = TodoDatabase.getDatabase(context)
        return RoomTaskLocalDataSource(database.taskDao(), dispatcher)
    }
}