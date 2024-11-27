package com.example.todoapp.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SummaryInfo(
    modifier: Modifier = Modifier,
    date: String = "March 9, 2024",
    taskSummaryStatistics: String = "5 tasks incomplete, 5 tasks completed"
) {
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        Text(
            text = date,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = taskSummaryStatistics,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

    }
}