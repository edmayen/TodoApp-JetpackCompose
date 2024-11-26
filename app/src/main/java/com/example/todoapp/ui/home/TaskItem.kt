package com.example.todoapp.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.domain.Category
import com.example.todoapp.domain.Task

@Composable
fun TaskItem(
    task: Task,
    modifier: Modifier = Modifier,
    onClickItem: (String) -> Unit,
    onToggleComplete: (Task) -> Unit,
    onDeleteItem: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable {
                onClickItem(task.id)
            }
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
            .padding(horizontal = 8.dp)
    ) {
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = {
                onToggleComplete(task)
            }
        )

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(8.dp)
                .weight(weight = 1f, fill = true)
        ) {
            Text(
                text = task.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall.copy(
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                ),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            if (!task.isCompleted) {
                task.description?.let {
                    Text(
                        text = it,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall.copy(
                            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                task.category?.let {
                    Text(
                        text = it.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Box {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        onDeleteItem(task.id)
                    }
            )
        }

    }
}

@Preview(
    showBackground = true
)
@Composable
fun Preview() {
    MaterialTheme {
        TaskItem(
            task = Task(
                id = "1",
                title = "Task 1",
                description = "Description 1",
                isCompleted = false,
                category = Category.WORK
            ),
            onClickItem = {},
            onToggleComplete = {},
            onDeleteItem = {}
        )
    }
}