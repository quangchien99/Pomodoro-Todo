package com.chpham.domain.interactor

import com.chpham.domain.SingleUseCaseWithParameter
import com.chpham.domain.model.Task
import com.chpham.domain.repository.TodoListRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case that retrieves a [Task] with the specified [id] from the [TodoListRepository].
 * @param todoListRepository The [TodoListRepository] to retrieve the [Task] from.
 *
 * @since March 25, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */
class GetTaskByIdUseCase(
    private val todoListRepository: TodoListRepository
) : SingleUseCaseWithParameter<Int, Flow<Task?>> {

    /**
     * Executes the use case by calling the [TodoListRepository.getTask] method with the specified [parameter].
     *
     * @param parameter The id of the [Task] to retrieve.
     * @return A [Flow] emitting the retrieved [Task], or null if the [Task] was not found.
     */
    override suspend fun execute(parameter: Int): Flow<Task?> {
        return todoListRepository.getTask(parameter)
    }
}
