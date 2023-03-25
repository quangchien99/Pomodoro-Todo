package com.chpham.domain.interactor

import com.chpham.domain.SingleUseCase
import com.chpham.domain.model.Task
import com.chpham.domain.repository.TodoListRepository
import kotlinx.coroutines.flow.Flow

/**
 * A use case class that retrieves all tasks from the [TodoListRepository].
 * It implements the [SingleUseCase] interface with the return type of a [Flow] of [List] of nullable [Task] objects.
 * @property todoListRepository the repository to retrieve the tasks from.
 *
 * @since March 25, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */
class GetAllTasksUseCase(
    private val todoListRepository: TodoListRepository
) : SingleUseCase<Flow<List<Task?>>> {

    /**
     * Executes the use case by calling the [TodoListRepository]'s [getTasks] method and returns the result.
     * @return a [Flow] of [List] of nullable [Task] objects.
     */
    override suspend fun execute(): Flow<List<Task?>> {
        return todoListRepository.getTasks()
    }
}
