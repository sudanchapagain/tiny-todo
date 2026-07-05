package np.com.sudanchapagain.tiny_todo.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import np.com.sudanchapagain.tiny_todo.data.TaskDatabase
import np.com.sudanchapagain.tiny_todo.data.TaskEntity

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val taskDao = TaskDatabase.getDatabase(application).taskDao()

    val tasks: StateFlow<List<TaskEntity>> =
        taskDao.getAllTasks().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addTask(title: String) {
        viewModelScope.launch {
            taskDao.insertTask(TaskEntity(title = title, isCompleted = false))
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            taskDao.deleteTask(task)
        }
    }

    fun updateTask(task: TaskEntity) {
        viewModelScope.launch {
            taskDao.insertTask(task)
        }
    }
}