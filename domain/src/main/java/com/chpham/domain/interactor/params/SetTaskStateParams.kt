package com.chpham.domain.interactor.params

import com.chpham.domain.model.TaskState

/**
 * Data class representing parameters to set task state.
 * @param id the ID of the task to update
 * @param taskState the new state to set the task to
 */
data class SetTaskStateParams(
    val id: Int,
    val taskState: TaskState
)
