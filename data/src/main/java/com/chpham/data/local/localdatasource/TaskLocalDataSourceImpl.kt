package com.chpham.data.local.localdatasource

import com.chpham.data.local.database.dao.TaskDao
import com.chpham.data.local.mapper.TaskMapper.toDomain
import com.chpham.data.local.mapper.TaskMapper.toEntity
import com.chpham.domain.model.Task
import com.chpham.domain.model.TaskState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of the [TaskLocalDataSource] interface that uses a [TaskDao] to access and manipulate local task data.
 * @param taskDao The [TaskDao] to use for data access.
 *
 * @since March 25, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */
class TaskLocalDataSourceImpl(private val taskDao: TaskDao) : TaskLocalDataSource {

    /**
     * Returns a [Flow] emitting a [Task] object with the specified ID, or null if no such task exists.
     * @param id The ID of the task to retrieve.
     * @return A [Flow] emitting a [Task] object with the specified ID, or null if no such task exists.
     */
    override fun getTask(id: Int): Flow<Task?> {
        return taskDao.getTask(id).map {
            it.toDomain()
        }
    }

    /**
     * Returns a [Flow] emitting a list of all [Task] objects in the local database.
     * @return A [Flow] emitting a list of all [Task] objects in the local database.
     */
    override fun getTasksByDay(dueDate: Long): Flow<List<Task?>> {
        return taskDao.getTasksOfDay(dueDate).map { listTaskEntity ->
            listTaskEntity.map { taskEntity ->
                taskEntity.toDomain()
            }
        }
    }

    /**
     * Returns a [Flow] emitting a list of all [Task] objects in the local database.
     * @return A [Flow] emitting a list of all [Task] objects in the local database.
     */
    override fun getTasksInRange(startDate: Long, endDate: Long): Flow<List<Task?>> {
        return taskDao.getTasksInRange(startDate, endDate).map { listTaskEntity ->
            listTaskEntity.map { taskEntity ->
                taskEntity.toDomain()
            }
        }
    }

    /**
     * Returns a [Flow] emitting a list of all [Task] objects in the local database.
     * @return A [Flow] emitting a list of all [Task] objects in the local database.
     */
    override fun getTasks(): Flow<List<Task?>> {
        return taskDao.getTasks().map { listTaskEntity ->
            listTaskEntity.map { taskEntity ->
                taskEntity.toDomain()
            }
        }
    }

    /**
     * Inserts the specified [Task] object into the local database.
     * @param task The [Task] object to insert.
     * @return The ID of the inserted task, or null if the insert failed.
     */
    override suspend fun insertTask(task: Task): Long? {
        return task.toEntity()?.let {
            taskDao.insertTask(it)
        }
    }

    /**
     * Updates the state of the task with the specified ID to the specified [TaskState].
     * @param id The ID of the task to update.
     * @param state The new state of the task.
     */
    override suspend fun setTaskState(id: Int, state: TaskState) {
        taskDao.setTaskState(id, state)
    }

    /**
     * Deletes the task with the specified ID from the local database.
     * @param id The ID of the task to delete.
     */
    override suspend fun deleteTask(id: Int) {
        taskDao.deleteTask(id)
    }

    /**
     * Updates the specified [Task] object in the local database.
     * @param task The [Task] object to update.
     */
    override suspend fun updateTask(task: Task): Long? {
        return task.toEntity()?.let {
            taskDao.updateTask(it)
        }
    }
}
