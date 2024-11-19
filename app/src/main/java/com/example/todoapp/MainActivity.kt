package com.example.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.todoapp.data.FakeTaskLocalDataSource
import com.example.todoapp.domain.Task
import com.example.todoapp.ui.theme.TODOAppTheme
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TODOAppTheme {
                var text by remember { mutableStateOf("") }
                val fakeLocalDataSource = FakeTaskLocalDataSource
                LaunchedEffect(true) {
                    fakeLocalDataSource.taskFlow.collect {
                        text = it.toString()
                    }
                }

                LaunchedEffect(true) {
                    fakeLocalDataSource.addTask(
                        Task(
                            id = UUID.randomUUID().toString(),
                            title = "Task 1",
                            description = "Description 1"
                        )
                    )
                    fakeLocalDataSource.addTask(
                        Task(
                            id = UUID.randomUUID().toString(),
                            title = "Task 2",
                            description = "Description 2"
                        )
                    )
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Text(
                        text = text,
                        modifier = Modifier
                            .padding(top = innerPadding.calculateTopPadding())
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}