package com.chpham.data.database.mapper

import com.chpham.data.database.entity.TaskEntity
import com.chpham.domain.model.RemindOptions
import com.chpham.domain.model.Task

/**
 * A mapper object that provides conversion functions between [TaskEntity] and [Task] data models.
 *
 * @since March 25, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */
object TaskMapper {

    /**
     * Converts a nullable [TaskEntity] object to a nullable [Task] object.
     * @param taskEntity the [TaskEntity] object to be converted.
     * @return a nullable [Task] object converted from the given [TaskEntity] object, or null if the given [TaskEntity] object is null.
     */
    fun TaskEntity?.toDomain(): Task? = this?.let {
        Task(
            id = it.id,
            name = it.name,
            timeCreated = it.timeCreated,
            state = it.state,
            priority = it.priority,
            dueDate = it.dueDate,
            remindOptions = it.remindOptions,
            timeFinished = it.timeFinished,
            deadline = it.deadline,
            remindBefore = it.remindBefore,
            category = it.category,
            description = it.description
        )
    }

    /**
     * Converts a nullable [Task] object to a nullable [TaskEntity] object.
     * @param task the [Task] object to be converted.
     * @return a nullable [TaskEntity] object converted from the given [Task] object, or null if the given [Task] object is null.
     */
    fun Task?.toEntity(): TaskEntity? = this?.let {
        TaskEntity(
            id = it.id,
            name = it.name,
            timeCreated = it.timeCreated,
            state = it.state,
            priority = it.priority,
            dueDate = it.dueDate,
            remindOptions = it.remindOptions
                ?: RemindOptions(RemindOptions.RemindMode.UN_SPECIFIED),
            timeFinished = it.timeFinished,
            deadline = it.deadline,
            remindBefore = it.remindBefore,
            category = it.category,
            description = it.description
        )
    }
}
