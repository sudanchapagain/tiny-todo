package np.com.sudanchapagain.tiny_todo.domain.repository

import kotlinx.coroutines.flow.Flow
import np.com.sudanchapagain.tiny_todo.data.entity.TaskEntity

interface TaskRepository {
    fun getTasks(): Flow<List<TaskEntity>>
    suspend fun insertTask(task: TaskEntity)
    suspend fun deleteTask(task: TaskEntity)
    suspend fun updateTask(task: TaskEntity)
}