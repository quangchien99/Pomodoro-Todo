package com.chpham.pomodoro_todo.base.viewmodel

/**
 * Enum class to hold the state of view model
 *
 * @since March 26, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */
enum class ViewModelState {
    INSERTING,
    INSERT_SUCCEEDED,
    LOADING,
    SUCCESS,
    ERROR
}