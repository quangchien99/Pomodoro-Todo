package com.chpham.pomodoro_todo.todo.ui.datetimepicker

import android.app.DatePickerDialog
import android.app.TimePickerDialog

class DateTimePickerImpl(
    private val settings: DateTimePickerSettings
) : DateTimePicker {

    private var datePickerDialog: DatePickerDialog? = null
    private var timePickerDialog: TimePickerDialog? = null


    override fun show() {
        showDatePicker(DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            showTimePicker(TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                settings.onDateTimeSetListener?.invoke(
                    year, month, dayOfMonth, hourOfDay, minute
                )
            })
        })
    }

    override fun dismiss() {
        datePickerDialog?.dismiss()
        timePickerDialog?.dismiss()
    }

    override fun isShowing(): Boolean {
        return datePickerDialog?.isShowing == true ||
                timePickerDialog?.isShowing == true
    }

    private fun showDatePicker(listener: DatePickerDialog.OnDateSetListener) {
        if (datePickerDialog == null) {
            datePickerDialog = DatePickerDialog(
                settings.context,
                settings.themeResId,
                listener,
                settings.initialYear,
                settings.initialMonth,
                settings.initialDay
            ).apply {
                setOnShowListener { settings.onShowListener?.invoke() }
                setOnCancelListener { settings.onDismissListener?.invoke() }
                settings.maxDate?.let { datePicker.maxDate = it }
                settings.minDate?.let { datePicker.minDate = it }
            }
        }
        datePickerDialog?.show()
    }

    private fun showTimePicker(listener: TimePickerDialog.OnTimeSetListener) {
        timePickerDialog = TimePickerDialog(
            settings.context,
            settings.themeResId,
            listener,
            settings.initialHour,
            settings.initialMinute,
            settings.is24HourView
        ).apply {
            setOnDismissListener { settings.onDismissListener?.invoke() }
        }
        timePickerDialog?.show()
    }

}