package com.chpham.pomodoro_todo.utils

import java.util.Calendar

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

/**
 * Converts a date represented as a Long value in milliseconds to a String in the format "day/month/year".
 * @return The date as a String in the format "day/month/year".
 */
fun Long.toDayMonthYearString(): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH) + 1 // add 1 because months are 0-indexed
    val year = calendar.get(Calendar.YEAR)
    return "$day/$month/$year"
}

// Define an extension function on the Long class that returns a time string in the format "hour:minute"
fun Long.toHourMinuteString(): String {
    val calendar = Calendar.getInstance() // create a Calendar object
    calendar.timeInMillis = this // set the time value in milliseconds

    val hour = calendar.get(Calendar.HOUR_OF_DAY) // get the hour value
    val minute = calendar.get(Calendar.MINUTE) // get the minute value

    return String.format(
        "%02d:%02d",
        hour,
        minute
    ) // format the hour and minute values into a string
}
