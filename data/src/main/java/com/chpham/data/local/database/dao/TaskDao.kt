package com.chpham.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.chpham.data.local.database.entity.TaskEntity
import com.chpham.domain.model.TaskState
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) interface for accessing and modifying TaskEntity data from a local database.
 *
 * @since March 25, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */
@Dao
interface TaskDao {

    /**
     * Retrieves a single task from the local database based on the provided task ID.
     * @param id The ID of the task to retrieve.
     * @return A [Flow] emitting the [TaskEntity] object matching the provided ID.
     */
    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTask(id: Int): Flow<TaskEntity>

    @Query("SELECT * FROM tasks WHERE dueDate = :dueDate AND (:category IS NULL OR category = :category)")
    fun getTasksOfDay(dueDate: Long, category: String? = null): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE dueDate BETWEEN :startDate AND :endDate")
    fun getTasksInRange(startDate: Long, endDate: Long): Flow<List<TaskEntity>>

    /**
     * Retrieves a list of all tasks in the local database, ordered by ID in ascending order.
     * @return A [Flow] emitting a list of all [TaskEntity] objects in the local database.
     */
    @Query("SELECT * FROM tasks ORDER BY id ASC")
    fun getTasks(): Flow<List<TaskEntity>>

    /**
     * Inserts a new [TaskEntity] object into the local database. If a task with the same ID already exists, it will be replaced.
     * @param task The [TaskEntity] object to insert or update in the local database.
     * @return The ID of the inserted or updated [TaskEntity] object.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    /**
     * Updates the state of an existing task in the local database based on the provided task ID.
     * @param id The ID of the task to update.
     * @param state The new [TaskState] to set for the task.
     */
    @Query("UPDATE tasks SET state = :state WHERE id = :id")
    suspend fun setTaskState(id: Int, state: TaskState)

    /**
     * Deletes a task from the local database based on the provided task ID.
     * @param id The ID of the task to delete.
     */
    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTask(id: Int)

    /**
     * Updates an existing [TaskEntity] object in the local database.
     * @param task The [TaskEntity] object to update in the local database.
     */
    @Update
    suspend fun updateTask(task: TaskEntity)
}
