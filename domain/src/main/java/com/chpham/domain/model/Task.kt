package com.chpham.domain.model

import java.util.Calendar

/**
 * Represents a task with various properties
 *
 * @since March 19, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */
data class Task(
    val id: Int = 0,
    val name: String,
    val timeCreated: Long,
    val state: TaskState = TaskState.TO_DO,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val dueDate: Long = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis,
    val remindOptions: RemindOptions? = null,
    val timeFinished: Long? = null,
    val deadline: Long? = null,
    val remindBefore: Int? = null,
    val category: String? = null,
    val description: String? = null
)
