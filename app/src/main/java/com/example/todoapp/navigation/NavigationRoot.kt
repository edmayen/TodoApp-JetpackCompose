package com.example.todoapp.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.todoapp.ui.screens.detail.TaskScreenRoot
import com.example.todoapp.ui.screens.detail.TaskViewModel
import com.example.todoapp.ui.screens.home.HomeScreenRoot
import com.example.todoapp.ui.screens.home.HomeScreenViewModel
import kotlinx.serialization.Serializable

@Composable
fun NavigationRoot(
    navController: NavHostController
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = HomeScreenDestination
        ) {
            composable<HomeScreenDestination> {
                val viewModel: HomeScreenViewModel = viewModel<HomeScreenViewModel>(
                    factory = HomeScreenViewModel.Factory
                )
                HomeScreenRoot(
                    onNavigateToTaskScreen = {
                        navController.navigate(TaskScreenDestination(it))
                    },
                    viewModel = viewModel
                )
            }

            composable<TaskScreenDestination> {
                val viewModel: TaskViewModel = viewModel<TaskViewModel>(
                    factory = TaskViewModel.Factory
                )
                TaskScreenRoot(
                    onNavigateBack = {
                        navController.navigateUp()
                    },
                    viewModel = viewModel
                )
            }
        }
    }
}

@Serializable
object HomeScreenDestination

@Serializable
data class TaskScreenDestination(
    val taskId: String? = null,
)