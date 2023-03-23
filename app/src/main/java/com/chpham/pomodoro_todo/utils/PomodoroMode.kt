package com.chpham.pomodoro_todo.utils

/**
 * An enumeration class that represents different modes of a Pomodoro timer.
 * Each mode is associated with a certain duration in minutes.
 * @property minutes The duration of the Pomodoro mode in minutes.
 * @constructor Creates a new instance of [PomodoroMode] with the specified [minutes] duration.
 * @param minutes The duration of the Pomodoro mode in minutes.
 * @see MODE_POMODORO
 * @see MODE_SHORT_BREAK
 * @see MODE_LONG_BREAK
 *
 * @since March 19, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */
enum class PomodoroMode(val minutes: Int) {
    /**
     * Represents the Pomodoro mode, which has a duration of 25 minutes.
     */
    MODE_POMODORO(25),

    /**
     * Represents the short break mode, which has a duration of 5 minutes.
     */
    MODE_SHORT_BREAK(5),

    /**
     * Represents the long break mode, which has a duration of 10 minutes.
     */
    MODE_LONG_BREAK(10)
}
