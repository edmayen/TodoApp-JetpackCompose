@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.example.todoapp.ui.screens.home

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapp.R
import com.example.todoapp.ui.screens.home.providers.HomeScreenPreviewProvider
import com.example.todoapp.ui.theme.TODOAppTheme

@Composable
fun HomeScreenRoot(
    onNavigateToTaskScreen: () -> Unit = {}
) {
    val viewModel = viewModel<HomeScreenViewModel>()
    val state = viewModel.state
    val event = viewModel.event
    val ctx = LocalContext.current

    LaunchedEffect(true) {
        event.collect { event ->
            when (event) {
                HomeScreenEvent.DeletedAllTasks -> {
                    Toast.makeText(
                        ctx,
                        ctx.getString(R.string.all_tasks_deleted),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                HomeScreenEvent.DeletedTask -> {
                    Toast.makeText(
                        ctx,
                        ctx.getString(R.string.tasks_deleted),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                HomeScreenEvent.UpdatedTask -> {
                    Toast.makeText(
                        ctx,
                        ctx.getString(R.string.tasks_updated),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    HomeScreen(
        state = state,
        onAction = { action ->
            when(action) {
                HomeScreenAction.OnAddTask -> {
                    onNavigateToTaskScreen()
                }
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeDataState,
    onAction: (HomeScreenAction) -> Unit = {}
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable {
                                isMenuExpanded = true
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Add task",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                        DropdownMenu(
                            expanded = isMenuExpanded,
                            onDismissRequest = { isMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    isMenuExpanded = false
                                    onAction(HomeScreenAction.OnDeleteAllTasks)
                                },
                                text = {
                                    Text(
                                        text = "Delete all tasks"
                                    )
                                }
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onAction(HomeScreenAction.OnAddTask)
                },
                content = {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Add task",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SummaryInfo(
                    date = state.date,
                    taskSummaryStatistics = state.summary,
                    completedTasks = state.completedTasks.size,
                    totalTasks = state.completedTasks.size + state.pendingTasks.size
                )
            }

            stickyHeader {
                SectionTitle(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surface
                        ),
                    title = "Completed Tasks"
                )
            }

            items(
                state.completedTasks,
                key = { task -> task.id }
            ) { task ->
                TaskItem(
                    task = task,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp)),
                    onClickItem = {},
                    onToggleComplete = { onAction(HomeScreenAction.OnToggleTask(task)) },
                    onDeleteItem = { onAction(HomeScreenAction.OnDeleteTask(task)) }
                )
            }

            stickyHeader {
                SectionTitle(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surface
                        ),
                    title = "Pending Tasks"
                )
            }

            items(
                state.pendingTasks,
                key = { task -> task.id }
            ) { task ->
                TaskItem(
                    task = task,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp)),
                    onClickItem = {},
                    onToggleComplete = { onAction(HomeScreenAction.OnToggleTask(task)) },
                    onDeleteItem = { onAction(HomeScreenAction.OnDeleteTask(task)) }
                )
            }
        }

    }
}

@Preview
@Composable
fun HomeScreenPreviewLight(
    @PreviewParameter(HomeScreenPreviewProvider::class) state: HomeDataState
) {
    TODOAppTheme {
        HomeScreen(
            state = HomeDataState(
                date = state.date,
                summary = state.summary,
                completedTasks = state.completedTasks,
                pendingTasks =  state.pendingTasks
            ),
            onAction = {}
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun HomeScreenPreviewDark(
    @PreviewParameter(HomeScreenPreviewProvider::class) state: HomeDataState
) {
    TODOAppTheme {
        HomeScreen(
            state = HomeDataState(
                date = state.date,
                summary = state.summary,
                completedTasks = state.completedTasks,
                pendingTasks =  state.pendingTasks
            ),
            onAction = {}
        )
    }
}

