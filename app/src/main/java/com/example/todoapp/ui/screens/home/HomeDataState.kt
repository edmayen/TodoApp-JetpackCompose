package com.example.todoapp.ui.screens.home

import com.example.todoapp.domain.Task

data class HomeDataState(
    val date: String = "",
    val summary: String = "",
    val completedTasks: List<Task> = emptyList(),
    val pendingTasks: List<Task> = emptyList()
)