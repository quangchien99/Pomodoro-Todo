package com.chpham.domain.interactor

import com.chpham.domain.SingleUseCase
import com.chpham.domain.SingleUseCaseWithParameter
import com.chpham.domain.interactor.params.GetTaskInRangeParams
import com.chpham.domain.model.Task
import com.chpham.domain.repository.TodoListRepository
import kotlinx.coroutines.flow.Flow

/**
 * A use case class that retrieves all tasks in selected range from the [TodoListRepository].
 * It implements the [SingleUseCase] interface with the return type of a [Flow] of [List] of nullable [Task] objects.
 * @property todoListRepository the repository to retrieve the tasks from.
 *
 * @since March 26, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */
class GetTasksInRangeUseCase(
    private val todoListRepository: TodoListRepository
) : SingleUseCaseWithParameter<GetTaskInRangeParams, Flow<List<Task?>>> {

    /**
     * Executes the use case by calling the [TodoListRepository]'s [getTasksInRange] method and returns the result.
     * @return a [Flow] of [List] of nullable [Task] objects.
     */
    override suspend fun execute(parameter: GetTaskInRangeParams): Flow<List<Task?>> {
        return todoListRepository.getTasksInRange(parameter.startDate, parameter.endDate)
    }

}