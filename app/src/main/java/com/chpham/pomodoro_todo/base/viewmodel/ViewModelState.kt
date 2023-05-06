package com.chpham.pomodoro_todo.base.viewmodel

/**
 * Enum class to hold the state of view model
 *
 * @since March 26, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */
sealed class ViewModelState {
    object INSERTING : ViewModelState()
    data class InsertSucceeded(val id: Int) : ViewModelState()
    data class UpdateSucceeded(val id: Int) : ViewModelState()
    object LOADING : ViewModelState()
    object SUCCESS : ViewModelState()
    object ERROR : ViewModelState()
}
