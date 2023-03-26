package com.chpham.pomodoro_todo.di

import com.chpham.domain.interactor.DeleteTaskUseCase
import com.chpham.domain.interactor.GetAllTasksUseCase
import com.chpham.domain.interactor.GetTaskByIdUseCase
import com.chpham.domain.interactor.GetTasksByDayUseCase
import com.chpham.domain.interactor.GetTasksInRangeUseCase
import com.chpham.domain.interactor.InsertTaskUseCase
import com.chpham.domain.interactor.SetTaskStateUseCase
import com.chpham.domain.interactor.UpdateTaskUseCase
import com.chpham.domain.repository.TodoListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Module responsible for providing instances of all UseCase classes used in the app, scoped to the ViewModel.
 *
 * @since March 25, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */
@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    /**
     * Provides an instance of [GetAllTasksUseCase] scoped to the ViewModel.
     * @param todoListRepository the [TodoListRepository] instance to be used by the use case.
     * @return an instance of [GetAllTasksUseCase].
     */
    @Provides
    @ViewModelScoped
    fun provideGetAllTasksUseCase(
        todoListRepository: TodoListRepository
    ) = GetAllTasksUseCase(todoListRepository)

    /**
     * Provides an instance of [GetTaskByIdUseCase] scoped to the ViewModel.
     * @param todoListRepository the [TodoListRepository] instance to be used by the use case.
     * @return an instance of [GetAllTasksUseCase].
     */
    @Provides
    @ViewModelScoped
    fun provideGetTaskByIdUseCase(
        todoListRepository: TodoListRepository
    ) = GetTaskByIdUseCase(todoListRepository)

    /**
     * Provides an instance of [GetTasksByDayUseCase] scoped to the ViewModel.
     * @param todoListRepository the [TodoListRepository] instance to be used by the use case.
     * @return an instance of [GetTasksByDayUseCase].
     */
    @Provides
    @ViewModelScoped
    fun provideTasksByDayUseCase(
        todoListRepository: TodoListRepository
    ) = GetTasksByDayUseCase(todoListRepository)

    /**
     * Provides an instance of [GetTasksInRangeUseCase] scoped to the ViewModel.
     * @param todoListRepository the [TodoListRepository] instance to be used by the use case.
     * @return an instance of [GetTasksInRangeUseCase].
     */
    @Provides
    @ViewModelScoped
    fun provideTasksInRangeUseCase(
        todoListRepository: TodoListRepository
    ) = GetTasksInRangeUseCase(todoListRepository)

    /**
     * Provides an instance of [InsertTaskUseCase] scoped to the ViewModel.
     * @param todoListRepository the [TodoListRepository] instance to be used by the use case.
     * @return an instance of [GetAllTasksUseCase].
     */
    @Provides
    @ViewModelScoped
    fun provideInsertTaskUseCase(
        todoListRepository: TodoListRepository
    ) = InsertTaskUseCase(todoListRepository)

    /**
     * Provides an instance of [SetTaskStateUseCase] scoped to the ViewModel.
     * @param todoListRepository the [TodoListRepository] instance to be used by the use case.
     * @return an instance of [GetAllTasksUseCase].
     */
    @Provides
    @ViewModelScoped
    fun provideSetTaskStateUseCase(
        todoListRepository: TodoListRepository
    ) = SetTaskStateUseCase(todoListRepository)

    /**
     * Provides an instance of [DeleteTaskUseCase] scoped to the ViewModel.
     * @param todoListRepository the [TodoListRepository] instance to be used by the use case.
     * @return an instance of [GetAllTasksUseCase].
     */
    @Provides
    @ViewModelScoped
    fun provideDeleteTaskUseCase(
        todoListRepository: TodoListRepository
    ) = DeleteTaskUseCase(todoListRepository)

    /**
     * Provides an instance of [UpdateTaskUseCase] scoped to the ViewModel.
     * @param todoListRepository the [TodoListRepository] instance to be used by the use case.
     * @return an instance of [GetAllTasksUseCase].
     */
    @Provides
    @ViewModelScoped
    fun provideUpdateTaskUseCase(
        todoListRepository: TodoListRepository
    ) = UpdateTaskUseCase(todoListRepository)
}
