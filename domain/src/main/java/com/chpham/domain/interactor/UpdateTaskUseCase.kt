package com.chpham.domain.interactor

import com.chpham.domain.SingleUseCaseWithParameter
import com.chpham.domain.model.Task
import com.chpham.domain.repository.TodoListRepository

/**
 * Use case that updates a task in the to-do list.
 * @param todoListRepository the repository to get the task from and update it
 *
 * @since March 25, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */
class UpdateTaskUseCase(private val todoListRepository: TodoListRepository) :
    SingleUseCaseWithParameter<Task, Unit?> {

    /**
     * Updates the specified task in the to-do list.
     *
     * @param parameter the task to update
     */
    override suspend fun execute(parameter: Task) {
        return todoListRepository.updateTask(parameter)
    }
}
