package com.example.todoapp.ui.screens.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.todoapp.R
import com.example.todoapp.domain.Task

@Composable
fun SummaryInfo(
    modifier: Modifier = Modifier,
    date: String = "March 9, 2024",
    taskSummaryStatistics: String = "5 tasks incomplete, 5 tasks completed",
    completedTasks: Int,
    totalTasks: Int
) {
    val angleRatio = remember {
        Animatable(0f)
    }
    LaunchedEffect(completedTasks, totalTasks) {
        if (totalTasks == 0) return@LaunchedEffect
        angleRatio.animateTo(
            targetValue = completedTasks/totalTasks.toFloat(),
            animationSpec = tween(
                durationMillis = 300
            )
        )
    }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = modifier
                .padding(16.dp)
                .weight(1.5f)
        ) {
            Text(
                text = date,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.summary_info, taskSummaryStatistics),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(16.dp)
                .aspectRatio(1f)
                .weight(1f)
        ) {
            val colorBase = MaterialTheme.colorScheme.inversePrimary
            val colorProgress = MaterialTheme.colorScheme.primary
            val strokeWidth = 16.dp
            Canvas(
                modifier = Modifier
                    .matchParentSize()
                    .aspectRatio(1f)
            ) {
                drawArc(
                    color = colorBase,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    size = size,
                    style = Stroke(
                        width = strokeWidth.toPx(),
                        cap = StrokeCap.Round
                    )
                )
                if (completedTasks <= totalTasks) {
                    drawArc(
                        color = colorProgress,
                        startAngle = 90f,
                        sweepAngle = 360f * angleRatio.value,
                        useCenter = false,
                        size = size,
                        style = Stroke(
                            width = strokeWidth.toPx(),
                            cap = StrokeCap.Round
                        )
                    )
                }
            }
            Text(
                text = "${(completedTasks.toFloat() / totalTasks.toFloat()).times(100).toInt()}%",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        }
    }
}