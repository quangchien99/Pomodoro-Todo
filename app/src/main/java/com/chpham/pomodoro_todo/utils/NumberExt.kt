package com.chpham.pomodoro_todo.utils

/**
 * @since March 19, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */


/**
 * A extension function on [Int] that converts seconds to minutes and returns a formatted string.
 * @return [String]
 */
fun Int.convertSecondsToMinutes(): String {
    val minutes = this / 60
    val secondsLeft = this % 60
    return String.format("%02d:%02d", minutes, secondsLeft)
}

/**
 * A extension function on [Int] that returns a formatted string representing the number of minutes.
 * @return [String]
 */
fun Int.getFormattedMinutes(): String {
    return "%02d:00".format(this)
}

/**
 * A extension function on [Int] that converts minutes to seconds.
 * @return Unit
 */
fun Int.convertMinutesToSeconds(): Int {
    return this * 60
}