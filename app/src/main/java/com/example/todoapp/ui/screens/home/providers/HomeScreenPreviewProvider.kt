package com.example.todoapp.ui.screens.home.providers

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.todoapp.domain.Category
import com.example.todoapp.domain.Task
import com.example.todoapp.ui.screens.home.HomeDataState

class HomeScreenPreviewProvider: PreviewParameterProvider<HomeDataState> {
    override val values: Sequence<HomeDataState>
        get() = sequenceOf(
            HomeDataState(
                date = "March 9, 2024",
                summary = "5 incomplete, 5 completed",
                completedTasks = completedTasks,
                pendingTasks = pendingTasks
            )
        )
}

val completedTasks = mutableListOf<Task>()
    .apply {
        repeat(20){
            add(
                Task(
                    id = it.toString(),
                    title = "Task $it",
                    description = "Description $it",
                    category = Category.WORK,
                    isCompleted = false
                )
            )
        }
    }

val pendingTasks = mutableListOf<Task>()
    .apply {
        repeat(20){
            add(
                Task(
                    id = (it+30).toString(),
                    title = "Task $it",
                    description = "Description $it",
                    category = Category.OTHER,
                    isCompleted = true
                )
            )
        }
    }