package com.chpham.domain.model

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
    val tag: Tag = Tag.GREEN,
    val state: TaskState = TaskState.TO_DO,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val timeFinished: Long? = null,
    val deadline: Long? = null,
    val timeToRemind: Long? = null,
    val category: String? = null,
    val description: String? = null
)
