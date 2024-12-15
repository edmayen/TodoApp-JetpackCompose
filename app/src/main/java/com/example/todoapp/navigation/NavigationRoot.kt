package com.example.todoapp.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.todoapp.ui.screens.detail.TaskScreenRoot
import com.example.todoapp.ui.screens.home.HomeScreenRoot
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
                HomeScreenRoot(
                    onNavigateToTaskScreen = {
                        navController.navigate(TaskScreenDestination)
                    }
                )
            }

            composable<TaskScreenDestination> {
                TaskScreenRoot(
                    onNavigateBack = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}

@Serializable
object HomeScreenDestination

@Serializable
object TaskScreenDestination