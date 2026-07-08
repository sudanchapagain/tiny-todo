package np.com.sudanchapagain.tiny_todo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import np.com.sudanchapagain.tiny_todo.data.entity.TaskEntity
import np.com.sudanchapagain.tiny_todo.domain.repository.TaskRepository

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    val tasks = repository.getTasks().stateIn(
            viewModelScope, SharingStarted.Lazily, emptyList()
        )

    fun addTask(title: String) {
        viewModelScope.launch {
            repository.insertTask(
                TaskEntity(title = title, isCompleted = false)
            )
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun updateTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }
}
