package com.chpham.data.repository

import com.chpham.data.database.localdatasource.TaskLocalDataSource
import com.chpham.domain.model.Task
import com.chpham.domain.model.TaskState
import com.chpham.domain.repository.TodoListRepository
import kotlinx.coroutines.flow.Flow

/**
 * Implementation of [TodoListRepository] that acts as an intermediary between the data sources and the rest of the application.
 * @param taskLocalDataSource Local data source for tasks.
 *
 * @since March 25, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */
class TodoListRepositoryImpl(
    private val taskLocalDataSource: TaskLocalDataSource
) : TodoListRepository {

    /**
     * Gets the task with the specified [id].
     * @param id The ID of the task to get.
     * @return A [Flow] emitting the task with the specified ID, or null if not found.
     */
    override fun getTask(id: Int): Flow<Task?> {
        return taskLocalDataSource.getTask(id)
    }

    /**
     * Gets all tasks.
     * @return A [Flow] emitting a list of all tasks, or an empty list if none exist.
     */
    override fun getTasks(): Flow<List<Task?>> {
        return taskLocalDataSource.getTasks()
    }

    /**
     * Inserts the specified [task] into the data source.
     * @param task The task to insert.
     * @return The ID of the inserted task, or null if the insertion failed.
     */
    override suspend fun insertTask(task: Task): Long? {
        return taskLocalDataSource.insertTask(task)
    }

    /**
     * Sets the state of the task with the specified [id] to the specified [state].
     * @param id The ID of the task to set the state of.
     * @param state The new state of the task.
     */
    override suspend fun setTaskState(id: Int, state: TaskState) {
        return taskLocalDataSource.setTaskState(id, state)
    }

    /**
     * Deletes the task with the specified [id] from the data source.
     * @param id The ID of the task to delete.
     */
    override suspend fun deleteTask(id: Int) {
        return taskLocalDataSource.deleteTask(id)
    }

    /**
     * Updates the specified [task] in the data source.
     * @param task The task to update.
     */
    override suspend fun updateTask(task: Task) {
        return taskLocalDataSource.updateTask(task)
    }
}
