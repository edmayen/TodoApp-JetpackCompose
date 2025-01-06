package com.example.todoapp.ui.screens.detail

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.example.todoapp.TodoApplication
import com.example.todoapp.data.FakeTaskLocalDataSource
import com.example.todoapp.domain.Task
import com.example.todoapp.domain.TaskLocalDataSource
import com.example.todoapp.navigation.TaskScreenDestination
import com.example.todoapp.ui.screens.home.HomeScreenViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.UUID

class TaskViewModel(
    savedStateHandle: SavedStateHandle,
    private val taskLocalDataSource: TaskLocalDataSource
): ViewModel() {


    val taskData = savedStateHandle.toRoute<TaskScreenDestination>()

    var state by mutableStateOf(TaskScreenState())
        private set

    private val eventsChannel = Channel<TaskEvent>()
    val events = eventsChannel.receiveAsFlow()

    private val canSaveTask = snapshotFlow {
        state.taskName.text.toString()
    }
    private var editedTask: Task? = null

    init {
        taskData.taskId?.let { taskId ->
            viewModelScope.launch {
                val task = taskLocalDataSource.getTaskById(taskId)
                editedTask = task

                state = state.copy(
                    taskName = TextFieldState(task?.title ?: ""),
                    taskDescription = TextFieldState(task?.description ?: ""),
                    isTaskDone = task?.isCompleted ?: false,
                    category = task?.category
                )
            }
        }
        canSaveTask.onEach {
            state = state.copy(
                canSaveTask = it.isNotEmpty()
            )
        }.launchIn(viewModelScope)
    }

    fun onAction(actionTask: ActionTask) {
        viewModelScope.launch {
            when (actionTask) {
                is ActionTask.ChangeTaskCategory -> {
                    state = state.copy(
                        category = actionTask.category
                    )
                }
                is ActionTask.ChangeTaskDone -> {
                    state = state.copy(
                        isTaskDone = actionTask.isTaskDone
                    )
                }
                is ActionTask.SaveTask -> {
                    editedTask?.let {
                        taskLocalDataSource.updateTask(
                            task = it.copy(
                                id = it.id,
                                title = state.taskName.text.toString(),
                                description = state.taskDescription.text.toString(),
                                isCompleted = state.isTaskDone,
                                category = state.category
                            )
                        )
                    }?: run {
                        val task = Task(
                            id = UUID.randomUUID().toString(),
                            title = state.taskName.text.toString(),
                            description = state.taskDescription.text.toString(),
                            isCompleted = state.isTaskDone,
                            category = state.category
                        )
                        taskLocalDataSource.addTask(task = task)
                    }
                    eventsChannel.send(TaskEvent.TaskCreated)
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
                TaskViewModel(
                    taskLocalDataSource = dataSource,
                    savedStateHandle = savedStateHandle
                )
            }
        }
    }

}