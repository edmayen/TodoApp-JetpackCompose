package com.example.todoapp.ui.screens.home

import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.FakeTaskLocalDataSource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel: ViewModel() {
    private val taskLocalDataSource = FakeTaskLocalDataSource

    var state by mutableStateOf(HomeDataState())
        private set

    private val eventChanel = Channel<HomeScreenEvent>()
    val event = eventChanel.receiveAsFlow()

    init {
        taskLocalDataSource.taskFlow
            .onEach { tasks ->
                val completedTasks = tasks.filter { it.isCompleted }
                val pendingTasks = tasks.filter { !it.isCompleted }
                state = state.copy(
                    date = "May 9, 2024",
                    summary = "You have ${pendingTasks.size} pending tasks",
                    completedTasks = completedTasks,
                    pendingTasks = pendingTasks
                )
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: HomeScreenAction) {
        viewModelScope.launch {
            when (action) {
                is HomeScreenAction.OnToggleTask -> {
                    val updatedTask = action.task.copy(isCompleted = !action.task.isCompleted)
                    taskLocalDataSource.updateTask(updatedTask)
                    eventChanel.send(HomeScreenEvent.UpdatedTask)
                }
                is HomeScreenAction.OnDeleteTask -> {
                    taskLocalDataSource.deleteTask(action.task)
                    eventChanel.send(HomeScreenEvent.DeletedTask)
                }
                is HomeScreenAction.OnDeleteAllTasks -> {
                    taskLocalDataSource.deleteAllTasks()
                    eventChanel.send(HomeScreenEvent.DeletedAllTasks)
                }
                else -> Unit
            }
        }
    }

}