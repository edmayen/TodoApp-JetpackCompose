package com.example.todoapp.data

import com.example.todoapp.domain.Task
import com.example.todoapp.domain.TaskLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoomTaskLocalDataSource @Inject constructor(
    private val taskDao: TaskDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): TaskLocalDataSource {

    override val taskFlow: Flow<List<Task>>
        get() = taskDao.getAllTasks().map { tasks ->
            tasks.map { taskEntity -> taskEntity.toTask() }
        }.flowOn(dispatcher)

    override suspend fun addTask(task: Task) = withContext(dispatcher) {
        taskDao.upsertTask(TaskEntity.fromTask(task))
    }

    override suspend fun updateTask(task: Task) = withContext(dispatcher) {
        taskDao.upsertTask(TaskEntity.fromTask(task))
    }

    override suspend fun deleteTask(task: Task) = withContext(dispatcher) {
        taskDao.deleteTaskById(task.id)
    }

    override suspend fun deleteAllTasks() = withContext(dispatcher) {
        taskDao.deleteAllTasks()
    }

    override suspend fun getTaskById(taskId: String): Task? = withContext(dispatcher) {
        taskDao.getTaskById(taskId)?.toTask()
    }
}