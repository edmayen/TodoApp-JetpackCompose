package com.example.todoapp.ui.screens.detail

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.UUID

class TaskViewModel: ViewModel() {

    private val fakeTaskLocalDataSource = com.example.todoapp.data.FakeTaskLocalDataSource

    var state by mutableStateOf(TaskScreenState())
        private set

    private val eventsChannel = Channel<TaskEvent>()
    val events = eventsChannel.receiveAsFlow()

    private val canSaveTask = snapshotFlow {
        state.taskName.text.toString()
    }

    init {
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
                    val task = com.example.todoapp.domain.Task(
                        id = UUID.randomUUID().toString(),
                        title = state.taskName.text.toString(),
                        description = state.taskDescription.text.toString(),
                        isCompleted = state.isTaskDone,
                        category = state.category
                    )
                    fakeTaskLocalDataSource.addTask(task = task)
                    eventsChannel.send(TaskEvent.TaskCreated)
                }
                else -> Unit
            }
        }

    }

}