package com.chpham.data.database

import androidx.room.Database
import androidx.room.TypeConverters
import com.chpham.data.database.dao.TaskDao
import com.chpham.data.database.entity.TaskEntity

/**
 * A Room database for Pomotodo app that contains a single table for tasks.
 * @constructor Creates a new instance of PomotodoDatabase
 * @param entities The array of entities (TaskEntity) to be included in the database
 * @param version The version of the database
 * @param converters The type converters used in the database
 *
 *
 * @since March 25, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */
@Database(entities = [TaskEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class PomotodoDatabase {

    /**
     * Returns an instance of TaskDao which provides access to the task table in the database.
     * @return An instance of TaskDao
     */
    abstract fun taskDao(): TaskDao
}
