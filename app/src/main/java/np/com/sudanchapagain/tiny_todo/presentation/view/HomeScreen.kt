package np.com.sudanchapagain.tiny_todo.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import np.com.sudanchapagain.tiny_todo.data.entity.TaskEntity
import np.com.sudanchapagain.tiny_todo.domain.model.Filter
import np.com.sudanchapagain.tiny_todo.domain.model.Task
import np.com.sudanchapagain.tiny_todo.presentation.component.FilterOption
import np.com.sudanchapagain.tiny_todo.presentation.component.SwipeToDeleteTaskItem
import np.com.sudanchapagain.tiny_todo.presentation.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val viewModel: TaskViewModel = hiltViewModel()

    val tasks by viewModel.tasks.collectAsState()
    var selectedFilter by remember { mutableStateOf(Filter.All) }
    var editingTask by remember { mutableStateOf<TaskEntity?>(null) }
    var isAdding by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var pendingDeletedIds by remember { mutableStateOf(setOf<Int>()) }
    var deleteGenerations by remember { mutableStateOf(mapOf<Int, Int>()) }

    val filteredTasks = tasks.filter { it.id !in pendingDeletedIds }.filter {
        selectedFilter == Filter.All || !it.isCompleted
    }

    fun deleteWithUndo(entity: TaskEntity) {
        coroutineScope.launch {
            deleteGenerations =
                deleteGenerations + (entity.id to (deleteGenerations[entity.id] ?: 0) + 1)
            pendingDeletedIds = pendingDeletedIds + entity.id
            val result = snackbarHostState.showSnackbar(
                message = "Task deleted",
                actionLabel = "Undo",
                duration = SnackbarDuration.Short,
                withDismissAction = true
            )
            pendingDeletedIds = pendingDeletedIds - entity.id
            if (result == SnackbarResult.Dismissed) {
                viewModel.deleteTask(entity)
            }
        }
    }

    if (editingTask != null || isAdding) {
        val task = editingTask?.let {
            Task(
                id = it.id,
                title = it.title,
                description = it.description,
                isCompleted = it.isCompleted,
                createdAt = it.createdAt
            )
        }
        TaskEditorScreen(task = task, onBack = {
            editingTask = null
            isAdding = false
        }, onSave = { title, description ->
            val entity = editingTask
            if (entity != null) {
                viewModel.updateTask(entity.copy(title = title, description = description))
            } else {
                viewModel.addTask(title, description)
            }
            editingTask = null
            isAdding = false
        }, onDelete = {
            val entity = editingTask
            editingTask = null
            isAdding = false
            if (entity != null) deleteWithUndo(entity)
        })
        return
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }, floatingActionButton = {
        Button(
            onClick = { isAdding = true }, modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add, contentDescription = "Add task"
            )
            Text(
                text = "New task", modifier = Modifier.padding(start = 8.dp)
            )
        }
    }) { paddingValues ->
        Column(
            modifier = modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(top = 2.dp, bottom = 8.dp)
            ) {
                Text(
                    "tiny todo",
                    fontWeight = FontWeight.Black,
                    style = MaterialTheme.typography.headlineLarge
                )
                Row {
                    FilterOption("All", Filter.All, selectedFilter) { selectedFilter = it }
                    FilterOption("Active", Filter.Active, selectedFilter) { selectedFilter = it }
                }
            }

            if (filteredTasks.isEmpty()) {
                EmptyState(
                    selectedFilter = selectedFilter,
                    modifier = Modifier.fillMaxSize().padding(bottom = 72.dp)
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = filteredTasks,
                        key = { "${it.id}_${deleteGenerations[it.id] ?: 0}" }) { taskEntity ->
                        val task = Task(
                            id = taskEntity.id,
                            title = taskEntity.title,
                            description = taskEntity.description,
                            isCompleted = taskEntity.isCompleted,
                            createdAt = taskEntity.createdAt
                        )

                        SwipeToDeleteTaskItem(task = task, onCheckedChange = { isChecked ->
                            viewModel.toggleTask(taskEntity, isChecked)
                        }, onDelete = {
                            deleteWithUndo(taskEntity)
                        }, onClick = { editingTask = taskEntity })
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyState(selectedFilter: Filter, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center, modifier = modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Checklist,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(72.dp)
            )
            Text(
                text = "No tasks yet",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = if (selectedFilter == Filter.Active) {
                    "You've finished everything. Nice!"
                } else {
                    "Tap New task to add your first task."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}