package com.chpham.pomodoro_todo.todo.ui.dialog

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.chpham.domain.model.RemindOptions
import com.chpham.pomodoro_todo.R
import com.chpham.pomodoro_todo.databinding.LayoutDatePickerBinding
import com.chpham.pomodoro_todo.databinding.LayoutRepeatOptionsBinding
import com.chpham.pomodoro_todo.utils.createAlertDialog
import java.util.Calendar

class DatePickerDialog(
    private val context: Context,
    private val layoutDatePickerBinding: LayoutDatePickerBinding,
    layoutRepeatOptionsBinding: LayoutRepeatOptionsBinding
) {

    private var selectedDate: Long

    private var selectedMode: RemindOptions.RemindMode
    private var selectedInterval: String?
    private var selectedRepeatIn: String?
    private var selectedEndInt: String?

    private lateinit var dateResultHandler: (Long, RemindOptions.RemindMode, String?, String?, String?) -> Unit

    private var datePickerAlertDialog: AlertDialog

    private var repeatOptionsDialog: RepeatOptionsDialog

    init {
        selectedDate = Calendar.getInstance().timeInMillis

        selectedMode = RemindOptions.RemindMode.UN_SPECIFIED
        selectedInterval = null
        selectedRepeatIn = null
        selectedEndInt = null

        datePickerAlertDialog = layoutDatePickerBinding.createAlertDialog(context)

        repeatOptionsDialog = RepeatOptionsDialog(context, layoutRepeatOptionsBinding)

        initListener()
    }

    fun handleSelectDate(
        currentDate: Long = Calendar.getInstance().timeInMillis,
        currentMode: RemindOptions.RemindMode = RemindOptions.RemindMode.UN_SPECIFIED,
        currentInterval: String? = null,
        currentRepeatIn: String? = null,
        currentEndInt: String? = null,
        dateResultHandler: (Long, RemindOptions.RemindMode, String?, String?, String?) -> Unit
    ) {
        this.dateResultHandler = dateResultHandler
        this.selectedMode = currentMode
        this.selectedInterval = currentInterval
        this.selectedRepeatIn = currentRepeatIn
        this.selectedEndInt = currentEndInt

        handlePreviousData(currentDate)

        datePickerAlertDialog.show()
    }

    private fun handlePreviousData(currentTime: Long = Calendar.getInstance().timeInMillis) {
        layoutDatePickerBinding.datePicker.date = currentTime
    }

    private fun initListener() {
        val today = Calendar.getInstance().timeInMillis

        with(layoutDatePickerBinding) {
            datePicker.minDate = today

            datePicker.setOnDateChangeListener { _, year, month, dayOfMonth ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, dayOfMonth)
                selectedDate = selectedCalendar.timeInMillis
            }

            tvRepeat.setOnClickListener {
                repeatOptionsDialog.handleSelectRepetition(
                    currentMode = selectedMode,
                    currentInterval = selectedInterval,
                    currentRepeatIn = selectedRepeatIn,
                    currentEndInt = selectedEndInt,
                    repeatResultHandler = { mode, interval, repeatIn, endIn ->
                        selectedMode = mode
                        selectedInterval = interval
                        selectedRepeatIn = repeatIn
                        selectedEndInt = endIn

                        when (mode) {
                            RemindOptions.RemindMode.DAILY ->
                                tvRepeatValue.text =
                                    context.getText(R.string.text_daily)
                            RemindOptions.RemindMode.WEEKLY ->
                                tvRepeatValue.text =
                                    context.getText(R.string.text_weekly)
                            RemindOptions.RemindMode.MONTHLY ->
                                tvRepeatValue.text =
                                    context.getText(R.string.text_monthly)
                            else -> {
                                resetView()
                            }
                        }
                    }
                )
            }

            btnCancel.setOnClickListener {
                resetView()
                datePickerAlertDialog.dismiss()
            }

            btnConfirm.setOnClickListener {
                dateResultHandler.invoke(
                    selectedDate, selectedMode, selectedInterval, selectedRepeatIn, selectedEndInt
                )
                datePickerAlertDialog.dismiss()
            }
        }
    }

    private fun LayoutDatePickerBinding.resetView() {
        tvRepeatValue.text = context.getString(R.string.text_none)
        selectedMode = RemindOptions.RemindMode.UN_SPECIFIED
        selectedInterval = null
        selectedRepeatIn = null
        selectedEndInt = null
    }
}
