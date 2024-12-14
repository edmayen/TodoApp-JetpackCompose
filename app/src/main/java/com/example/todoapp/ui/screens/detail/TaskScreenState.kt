package com.example.todoapp.ui.screens.detail

import androidx.compose.foundation.text.input.TextFieldState
import com.example.todoapp.domain.Category

data class TaskScreenState(
    val taskName: TextFieldState = TextFieldState(),
    val taskDescription: TextFieldState = TextFieldState(),
    val isTaskDone: Boolean = false,
    val category: Category? = null,
    val canSaveTask: Boolean = false
)
