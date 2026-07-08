package np.com.sudanchapagain.tiny_todo.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import np.com.sudanchapagain.tiny_todo.data.entity.TaskEntity

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: Int)
}
