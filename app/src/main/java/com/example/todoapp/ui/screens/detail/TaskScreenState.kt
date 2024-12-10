package com.example.todoapp.ui.screens.detail

import com.example.todoapp.domain.Category

data class TaskScreenState(
    val taskName: String = "",
    val taskDescription: String = "",
    val isTaskDone: Boolean = false,
    val category: Category? = null,
)
