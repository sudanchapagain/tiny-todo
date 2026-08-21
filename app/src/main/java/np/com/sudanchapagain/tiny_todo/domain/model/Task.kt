package np.com.sudanchapagain.tiny_todo.domain.model

data class Task(
    val id: Int,
    val title: String,
    val description: String = "",
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)