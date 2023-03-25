package com.chpham.domain.interactor

import com.chpham.domain.SingleUseCaseWithParameter
import com.chpham.domain.interactor.params.SetTaskStateParams
import com.chpham.domain.repository.TodoListRepository

/**
 * A use case class that sets the state of a task.
 * @param todoListRepository An instance of [TodoListRepository] which is used to update the state of the task.
 *
 * @since March 25, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */
class SetTaskStateUseCase(
    private val todoListRepository: TodoListRepository
) : SingleUseCaseWithParameter<SetTaskStateParams, Unit> {

    /**
     * Executes the use case by setting the state of the task in [todoListRepository] with the given [parameter].
     * @param parameter A [SetTaskStateParams] object containing the id of the task to be updated and its new state.
     */
    override suspend fun execute(parameter: SetTaskStateParams) {
        todoListRepository.setTaskState(
            id = parameter.id,
            state = parameter.taskState
        )
    }
}
