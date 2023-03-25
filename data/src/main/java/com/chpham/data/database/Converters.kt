package com.chpham.data.database

import androidx.room.TypeConverter
import com.chpham.domain.model.RemindOptions
import com.chpham.domain.model.TaskPriority
import com.chpham.domain.model.TaskState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * A collection of [TypeConverter] methods for converting custom data types to and from their
 * serialized string representations, which can be stored in a Room database.
 *
 * @since March 25, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */
class Converters {

    /**
     * Converts a [TaskState] enum to a string representation.
     *
     * @param taskState The [TaskState] enum to convert.
     * @return The string representation of the [TaskState] enum, or null if the input is null.
     */
    @TypeConverter
    fun toStateString(taskState: TaskState?): String? = taskState?.name

    /**
     * Converts a string representation of a [TaskState] enum to the corresponding enum value.
     *
     * @param name The string representation of the [TaskState] enum.
     * @return The [TaskState] enum value, or [TaskState.TO_DO] if the input is null.
     */
    @TypeConverter
    fun toTaskState(name: String?): TaskState =
        name?.let { enumValueOf<TaskState>(it) } ?: TaskState.TO_DO

    /**
     * Converts a [TaskPriority] enum to a string representation.
     *
     * @param taskPriority The [TaskPriority] enum to convert.
     * @return The string representation of the [TaskPriority] enum, or null if the input is null.
     */
    @TypeConverter
    fun toPriorityString(taskPriority: TaskPriority?): String? = taskPriority?.name

    /**
     * Converts a string representation of a [TaskPriority] enum to the corresponding enum value.
     *
     * @param priority The string representation of the [TaskPriority] enum.
     * @return The [TaskPriority] enum value, or [TaskPriority.LOW] if the input is null.
     */
    @TypeConverter
    fun toTaskPriority(priority: String?): TaskPriority =
        priority?.let { enumValueOf<TaskPriority>(it) } ?: TaskPriority.LOW

    /**
     * Converts a [RemindOptions] object to its JSON string representation using Gson.
     *
     * @param remindOptions The [RemindOptions] object to convert.
     * @return The JSON string representation of the [RemindOptions] object, or null if the input is null.
     */
    @TypeConverter
    fun toRemindOptionsString(remindOptions: RemindOptions?): String? {
        if (remindOptions == null) {
            return null
        }
        return Gson().toJson(remindOptions)
    }

    /**
     * Converts a JSON string representation of a [RemindOptions] object to the corresponding object
     * using Gson.
     *
     * @param remindOption The JSON string representation of the [RemindOptions] object.
     * @return The [RemindOptions] object, or null if the input is null.
     */
    @TypeConverter
    fun toRemindOptions(remindOption: String?): RemindOptions? {
        if (remindOption == null) {
            return null
        }
        val type = object : TypeToken<RemindOptions>() {}.type
        return Gson().fromJson(remindOption, type)
    }
}
