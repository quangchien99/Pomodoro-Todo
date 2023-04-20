package com.chpham.pomodoro_todo.todo.ui.dialog

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.chpham.pomodoro_todo.R
import com.chpham.pomodoro_todo.databinding.LayoutTimePickerBinding
import com.chpham.pomodoro_todo.utils.createAlertDialog
import java.util.Calendar

class ReminderDialog(
    context: Context,
    private val layoutTimePickerBinding: LayoutTimePickerBinding
) {
    private var selectedTime: Long
    private var selectedRemindBefore: Int

    private val remindBeforeViewsMap: Map<Int, TextView>

    private val reminderAlertDialog: AlertDialog

    private lateinit var remindResultHandler: (Long, Int) -> Unit

    init {
        selectedTime = System.currentTimeMillis()
        selectedRemindBefore = 0

        remindBeforeViewsMap = mapOf(
            0 to layoutTimePickerBinding.tvNone,
            5 to layoutTimePickerBinding.tv5Minutes,
            10 to layoutTimePickerBinding.tv10Minutes,
            15 to layoutTimePickerBinding.tv15Minutes,
            30 to layoutTimePickerBinding.tv30Minutes,
            60 to layoutTimePickerBinding.tv1Hour,
            120 to layoutTimePickerBinding.tv2Hours,
            180 to layoutTimePickerBinding.tv3Hours
        )

        reminderAlertDialog = layoutTimePickerBinding.createAlertDialog(context)

        initListener()
    }

    fun handleSetUpReminder(
        currentTime: Long,
        currentRemindBefore: Int,
        remindResultHandler: (Long, Int) -> Unit
    ) {
        this.remindResultHandler = remindResultHandler

        handlePreviousData(currentTime, currentRemindBefore)

        reminderAlertDialog.show()
    }

    private fun initListener() {
        val calendar = Calendar.getInstance()

        with(layoutTimePickerBinding) {
            timerPicker.setOnTimeChangedListener { _, hourOfDay, minute ->
                val currentTime = System.currentTimeMillis()
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                calendar.set(Calendar.SECOND, 0)
                if (calendar.timeInMillis >= currentTime + 10 * 60 * 1000) {
                    tvErrorTimer.visibility = View.GONE
                    selectedTime = calendar.timeInMillis
                } else {
                    tvErrorTimer.visibility = View.VISIBLE
                }
            }

            setReminderClickListener(tvNone, 0)
            setReminderClickListener(tv5Minutes, 5)
            setReminderClickListener(tv10Minutes, 10)
            setReminderClickListener(tv15Minutes, 15)
            setReminderClickListener(tv30Minutes, 30)
            setReminderClickListener(tv1Hour, 60)
            setReminderClickListener(tv2Hours, 120)
            setReminderClickListener(tv3Hours, 180)

            layoutTimePickerBinding.switchReminder.setOnClickListener {
                if (layoutTimePickerBinding.switchReminder.isChecked) {
                    tv5Minutes.performClick()
                } else {
                    tvNone.performClick()
                    selectedTime = System.currentTimeMillis()
                }
            }

            btnCancel.setOnClickListener {
                reminderAlertDialog.dismiss()
            }

            btnConfirm.setOnClickListener {
                if (switchReminder.isChecked) {
                    val currentTimeInMillis = System.currentTimeMillis()
                    val futureTimeInMillis = selectedTime - (selectedRemindBefore * 60 * 1000)
                    if (selectedTime <= System.currentTimeMillis() + 10 * 60 * 1000) {
                        layoutTimePickerBinding.tvErrorTimer.visibility = View.VISIBLE
                    } else if (futureTimeInMillis <= currentTimeInMillis && selectedRemindBefore != 0) {
                        layoutTimePickerBinding.tvErrorRemindBefore.visibility = View.VISIBLE
                    } else {
                        reminderAlertDialog.dismiss()
                        remindResultHandler.invoke(selectedTime, selectedRemindBefore)
                    }
                } else {
                    reminderAlertDialog.dismiss()
                    remindResultHandler.invoke(selectedTime, selectedRemindBefore)
                }
            }
        }
    }

    private fun handlePreviousData(
        currentTime: Long,
        currentRemindBefore: Int
    ) {
        selectedTime = currentTime
        selectedRemindBefore = currentRemindBefore

        remindBeforeViewsMap.values.forEach {
            it.setBackgroundResource(R.drawable.bg_btn_date_unselected)
        }
        remindBeforeViewsMap[selectedRemindBefore]?.performClick()

        val calendar = Calendar.getInstance().apply {
            timeInMillis = selectedTime
        }

        layoutTimePickerBinding.timerPicker.apply {
            this.hour = calendar.get(Calendar.HOUR_OF_DAY)
            this.minute = calendar.get(Calendar.MINUTE)
        }
    }

    private fun setReminderClickListener(textView: TextView, minutes: Int) {
        textView.setOnClickListener {
            val currentTimeInMillis = System.currentTimeMillis()
            val futureTimeInMillis = selectedTime - (minutes * 60 * 1000)
            if (futureTimeInMillis >= currentTimeInMillis || minutes == 0) {
                layoutTimePickerBinding.tvErrorRemindBefore.visibility = View.GONE
                unSelectPreviousOptions(selectedRemindBefore)
                layoutTimePickerBinding.tvReminderValue.text = textView.text
                textView.setBackgroundResource(R.drawable.bg_btn_date_selected)
                selectedRemindBefore = minutes
                layoutTimePickerBinding.switchReminder.isChecked = minutes != 0
            } else {
                layoutTimePickerBinding.tvErrorRemindBefore.visibility = View.VISIBLE
            }
        }
    }

    private fun unSelectPreviousOptions(remindBefore: Int) {
        remindBeforeViewsMap[remindBefore]?.setBackgroundResource(R.drawable.bg_btn_date_unselected)
    }
}
