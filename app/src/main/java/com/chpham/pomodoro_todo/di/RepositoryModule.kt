package com.chpham.pomodoro_todo.di

import com.chpham.data.local.localdatasource.TaskLocalDataSource
import com.chpham.data.repository.TodoListRepositoryImpl
import com.chpham.domain.repository.TodoListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides a singleton instance of [TodoListRepository].
 *
 * @since March 25, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /**
     * Provides a singleton instance of [TodoListRepository] by injecting
     * @param taskLocalDataSource the [TaskLocalDataSource] instance to be injected into [TodoListRepositoryImpl].
     * @return the singleton instance of [TodoListRepository].
     */
    @Provides
    @Singleton
    fun provideTaskRepository(
        taskLocalDataSource: TaskLocalDataSource
    ): TodoListRepository = TodoListRepositoryImpl(taskLocalDataSource)
}
