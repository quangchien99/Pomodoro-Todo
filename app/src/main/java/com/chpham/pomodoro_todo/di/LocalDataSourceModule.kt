package com.chpham.pomodoro_todo.di

import com.chpham.data.database.dao.TaskDao
import com.chpham.data.database.localdatasource.TaskLocalDataSource
import com.chpham.data.database.localdatasource.TaskLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides a singleton instance of [TaskLocalDataSource] interface,
 * which is implemented by [TaskLocalDataSourceImpl] class using [TaskDao] to perform local data operations.
 *
 * @since March 25, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */
@Module
@InstallIn(SingletonComponent::class)
object LocalDataSourceModule {

    /**
     * Provides a singleton instance of [TaskLocalDataSource] interface.
     * @param taskDao an instance of [TaskDao] used to perform local data operations.
     * @return a singleton instance of [TaskLocalDataSource] interface, implemented by [TaskLocalDataSourceImpl].
     */
    @Provides
    @Singleton
    fun provideTaskLocalDataSource(
        taskDao: TaskDao
    ): TaskLocalDataSource = TaskLocalDataSourceImpl(taskDao)
}
