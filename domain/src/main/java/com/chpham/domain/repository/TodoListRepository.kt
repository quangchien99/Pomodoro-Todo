package com.chpham.domain.repository

import com.chpham.domain.model.Task
import com.chpham.domain.model.TaskState
import kotlinx.coroutines.flow.Flow

interface TodoListRepository {

    fun getTask(id: Int): Flow<Task?>
    fun getTasksByDayAndCategory(dueDate: Long, category: String? = null): Flow<List<Task?>>
    fun getTasksInRange(startDate: Long, endDate: Long): Flow<List<Task?>>

    fun getTasks(): Flow<List<Task?>>

    suspend fun insertTask(task: Task): Long?

    suspend fun setTaskState(id: Int, state: TaskState)

    suspend fun deleteTask(id: Int)

    suspend fun updateTask(task: Task)
}
