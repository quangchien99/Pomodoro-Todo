package com.chpham.domain.interactor

import com.chpham.domain.SingleUseCaseWithParameter
import com.chpham.domain.repository.TodoListRepository

/**
 * Use case that deletes a task from the task list.
 * @param todoListRepository repository that provides access to task data.
 *
 * @since March 25, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */
class DeleteTaskUseCase(private val todoListRepository: TodoListRepository) :
    SingleUseCaseWithParameter<Int, Unit> {

    /**
     * Executes the use case to delete a task with the given [parameter] from the task list.
     * @param parameter the ID of the task to be deleted.
     */
    override suspend fun execute(parameter: Int) {
        todoListRepository.deleteTask(parameter)
    }
}
