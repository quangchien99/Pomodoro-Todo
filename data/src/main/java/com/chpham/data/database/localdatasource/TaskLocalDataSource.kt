package com.chpham.data.database.localdatasource

import com.chpham.domain.model.Task
import com.chpham.domain.model.TaskState
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining methods for accessing and manipulating local task data.
 *
 * @since March 25, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */
interface TaskLocalDataSource {

    /**
     * Returns a [Flow] emitting a [Task] object with the specified ID, or null if no such task exists.
     * @param id The ID of the task to retrieve.
     * @return A [Flow] emitting a [Task] object with the specified ID, or null if no such task exists.
     */
    fun getTask(id: Int): Flow<Task?>

    /**
     * Returns a [Flow] emitting a list of all [Task] objects in the local database.
     * @return A [Flow] emitting a list of all [Task] objects in the local database.
     */
    fun getTasks(): Flow<List<Task?>>

    /**
     * Inserts the specified [Task] object into the local database.
     * @param task The [Task] object to insert.
     * @return The ID of the inserted task, or null if the insert failed.
     */
    suspend fun insertTask(task: Task): Long?

    /**
     * Updates the state of the task with the specified ID to the specified [TaskState].
     * @param id The ID of the task to update.
     * @param state The new state of the task.
     */
    suspend fun setTaskState(id: Int, state: TaskState)

    /**
     * Deletes the task with the specified ID from the local database.
     * @param id The ID of the task to delete.
     */
    suspend fun deleteTask(id: Int)

    /**
     * Updates the specified [Task] object in the local database.
     * @param task The [Task] object to update.
     */
    suspend fun updateTask(task: Task)
}
