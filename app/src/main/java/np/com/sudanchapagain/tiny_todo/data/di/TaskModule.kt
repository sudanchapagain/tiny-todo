package np.com.sudanchapagain.tiny_todo.data.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import np.com.sudanchapagain.tiny_todo.data.dao.TaskDao
import np.com.sudanchapagain.tiny_todo.data.database.TaskDatabase
import np.com.sudanchapagain.tiny_todo.data.repository.TaskRepositoryImpl
import np.com.sudanchapagain.tiny_todo.domain.repository.TaskRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TaskModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): TaskDatabase {
        return Room.databaseBuilder(
            context,
            TaskDatabase::class.java,
            "task_database"
        ).build()
    }

    @Provides
    fun provideTaskDao(
        db: TaskDatabase
    ): TaskDao = db.taskDao()

    @Provides
    fun provideTaskRepository(
        dao: TaskDao
    ): TaskRepository = TaskRepositoryImpl(dao)
}
