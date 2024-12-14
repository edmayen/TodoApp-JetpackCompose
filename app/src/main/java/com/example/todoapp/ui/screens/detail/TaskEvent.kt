package com.example.todoapp.ui.screens.detail

sealed interface TaskEvent {
    data object TaskCreated : TaskEvent
}