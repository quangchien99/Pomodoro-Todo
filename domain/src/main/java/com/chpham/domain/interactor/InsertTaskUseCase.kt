package com.chpham.domain.interactor

import com.chpham.domain.SingleUseCaseWithParameter
import com.chpham.domain.model.Task
import com.chpham.domain.repository.TodoListRepository

/**
 * Use case for inserting a new task into the todo list.
 * @property todoListRepository The repository for accessing the todo list.
 *
 * @since March 25, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */
class InsertTaskUseCase(
    private val todoListRepository: TodoListRepository
) : SingleUseCaseWithParameter<Task, Long?> {

    /**
     * Inserts a new task into the to do list.
     *
     * @param parameter The task to be inserted.
     * @return The ID of the inserted task, or null if insertion failed.
     */
    override suspend fun execute(parameter: Task): Long? {
        return todoListRepository.insertTask(parameter)
    }
}
