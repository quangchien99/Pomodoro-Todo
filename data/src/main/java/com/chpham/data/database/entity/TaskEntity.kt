package com.chpham.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chpham.domain.model.RemindOptions
import com.chpham.domain.model.TaskPriority
import com.chpham.domain.model.TaskState

/**
 * Entity class representing a single task in the local database.
 * @param id The unique ID of the task. This field is auto-generated by the database.
 * @param name The name or title of the task.
 * @param timeCreated The time, in milliseconds since the Unix epoch, when the task was created.
 * @param state The current state of the task. Defaults to [TaskState.TO_DO].
 * @param priority The priority level of the task. Defaults to [TaskPriority.MEDIUM].
 * @param remindOptions The options for reminding the user about the task. Defaults to [RemindOptions] with [RemindOptions.RemindMode.UN_SPECIFIED].
 * @param timeFinished The time, in milliseconds since the Unix epoch, when the task was completed. Defaults to null.
 * @param deadline The time, in milliseconds since the Unix epoch, by which the task should be completed. Defaults to null.
 * @param remindBefore The amount of time, in minutes, before the deadline at which the user should be reminded about the task. Defaults to null.
 * @param category The category to which the task belongs. Defaults to null.
 * @param description A description or notes for the task. Defaults to null.
 *
 * @since March 25, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */
@Entity(tableName = "Task")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "timeCreated")
    val timeCreated: Long,
    @ColumnInfo(name = "state")
    val state: TaskState = TaskState.TO_DO,
    @ColumnInfo(name = "priority")
    val priority: TaskPriority = TaskPriority.MEDIUM,
    @ColumnInfo(name = "remindOptions")
    val remindOptions: RemindOptions = RemindOptions(RemindOptions.RemindMode.UN_SPECIFIED),
    @ColumnInfo(name = "timeFinished")
    val timeFinished: Long? = null,
    @ColumnInfo(name = "deadline")
    val deadline: Long? = null,
    @ColumnInfo(name = "remindBefore")
    val remindBefore: Int? = null,
    @ColumnInfo(name = "category")
    val category: String? = null,
    @ColumnInfo(name = "description")
    val description: String? = null
)
