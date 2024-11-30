package com.example.todoapp.ui.screens.home

sealed class HomeScreenEvent {
    data object UpdatedTask: HomeScreenEvent()
    data object DeletedAllTasks: HomeScreenEvent()
    data object DeletedTask: HomeScreenEvent()
}