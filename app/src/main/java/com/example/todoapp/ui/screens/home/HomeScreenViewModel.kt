package com.example.todoapp.ui.screens.home

import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.todoapp.TodoApplication
import com.example.todoapp.domain.TaskLocalDataSource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val taskLocalDataSource: TaskLocalDataSource
): ViewModel() {

    var state by mutableStateOf(HomeDataState())
        private set

    private val eventChanel = Channel<HomeScreenEvent>()
    val event = eventChanel.receiveAsFlow()

    init {
        state = state.copy(
            date = LocalDate.now().let {
                DateTimeFormatter.ofPattern("EEEE, MMMM dd yyyy").format(it)
            }
        )

        taskLocalDataSource.taskFlow
            .onEach { tasks ->
                val completedTasks = tasks
                    .filter { it.isCompleted }
                    .sortedByDescending { task -> task.date }
                val pendingTasks = tasks
                    .filter { !it.isCompleted }
                    .sortedByDescending { task -> task.date }
                state = state.copy(
                    summary = pendingTasks.size.toString(),
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

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val dataSource = (this[APPLICATION_KEY] as TodoApplication).dataSource
                HomeScreenViewModel(
                    taskLocalDataSource = dataSource,
                    savedStateHandle = savedStateHandle
                )
            }
        }
    }

}