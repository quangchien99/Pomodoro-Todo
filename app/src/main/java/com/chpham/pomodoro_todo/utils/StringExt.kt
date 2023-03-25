package com.chpham.pomodoro_todo.utils

import android.content.Context
import com.chpham.pomodoro_todo.R

/**
 * @since March 19, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */

fun String.convertToDays(context: Context): Int {
    val parts = this.split(" ")
    val num = parts[0].toInt()
    return when (parts[1]) {
        context.getString(R.string.text_day), context.getString(R.string.text_days) -> num
        context.getString(R.string.text_week), context.getString(R.string.text_weeks) -> num * 7
        context.getString(R.string.text_month), context.getString(R.string.text_months) -> num * 30 // assuming 30 days in a month
        else -> 0 // invalid unit
    }
}

fun String.getDay(): Int {
    val parts = this.split(" ")
    return parts[1].toInt()
}
