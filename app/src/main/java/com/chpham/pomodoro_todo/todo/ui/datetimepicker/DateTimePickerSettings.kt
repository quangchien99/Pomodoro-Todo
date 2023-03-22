package com.chpham.pomodoro_todo.todo.ui.datetimepicker

import android.content.Context
import java.util.Calendar

class DateTimePickerSettings(val context: Context) {
    var onDateTimeSetListener: OnDateTimeSetListener? = null
    var onShowListener: OnShowListener? = null
    var onDismissListener: OnDismissListener? = null
    var themeResId: Int = 0
    var initialYear: Int = Calendar.getInstance()[Calendar.YEAR]
    var initialMonth: Int = Calendar.getInstance()[Calendar.MONTH]
    var initialDay: Int = Calendar.getInstance()[Calendar.DAY_OF_MONTH]
    var initialHour: Int = 0
    var initialMinute: Int = 0
    var is24HourView: Boolean = true
    var maxDate: Long? = null
    var minDate: Long? = null
}