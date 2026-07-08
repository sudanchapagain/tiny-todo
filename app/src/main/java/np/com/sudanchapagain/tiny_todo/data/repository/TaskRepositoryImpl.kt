package np.com.sudanchapagain.tiny_todo.data.repository

import np.com.sudanchapagain.tiny_todo.data.dao.TaskDao
import np.com.sudanchapagain.tiny_todo.data.entity.TaskEntity
import np.com.sudanchapagain.tiny_todo.domain.repository.TaskRepository
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val dao: TaskDao
) : TaskRepository {

    override fun getTasks() = dao.getAllTasks()

    override suspend fun insertTask(task: TaskEntity) {
        dao.insertTask(task)
    }

    override suspend fun deleteTask(task: TaskEntity) {
        dao.deleteTask(task)
    }

    override suspend fun updateTask(task: TaskEntity) {
        dao.insertTask(task)
    }
}