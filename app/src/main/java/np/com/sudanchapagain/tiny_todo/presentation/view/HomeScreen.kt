package np.com.sudanchapagain.tiny_todo.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import np.com.sudanchapagain.tiny_todo.domain.model.Filter
import np.com.sudanchapagain.tiny_todo.domain.model.Task
import np.com.sudanchapagain.tiny_todo.presentation.component.AddTaskBottomSheet
import np.com.sudanchapagain.tiny_todo.presentation.component.FilterOption
import np.com.sudanchapagain.tiny_todo.presentation.component.SwipeToDeleteTaskItem
import np.com.sudanchapagain.tiny_todo.presentation.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val viewModel: TaskViewModel = hiltViewModel()

    val tasks by viewModel.tasks.collectAsState()
    var filter by remember { mutableStateOf(Filter.All) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false }, sheetState = sheetState
        ) {
            AddTaskBottomSheet(
                onAdd = { title ->
                    viewModel.addTask(title)
                    showBottomSheet = false
                })
        }
    }

    Column(modifier = modifier.padding(16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
        ) {
            Text(
                "tiny todo",
                fontWeight = FontWeight.Black,
                style = MaterialTheme.typography.titleLarge
            )
            Row {
                FilterOption("All", Filter.All, filter) { filter = it }
                FilterOption("Active", Filter.Active, filter) { filter = it }
            }
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(
                items = tasks.filter { filter == Filter.All || !it.isCompleted },
                key = { it.id }) { taskEntity ->
                val task = Task(title = taskEntity.title, isCompleted = taskEntity.isCompleted)
                SwipeToDeleteTaskItem(task = task, onCheckedChange = { isChecked ->
                    viewModel.updateTask(taskEntity.copy(isCompleted = isChecked))
                }, onDelete = { viewModel.deleteTask(taskEntity) })
            }
        }

        Button(
            onClick = { showBottomSheet = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                modifier = Modifier.padding(
                    start = 16.dp, end = 16.dp
                ),
                text = "Add task",
            )
        }
    }
}
