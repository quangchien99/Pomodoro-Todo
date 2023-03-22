package com.chpham.pomodoro_todo.todo.ui

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.chpham.domain.model.RemindOptions
import com.chpham.domain.model.Tag
import com.chpham.domain.model.TaskPriority
import com.chpham.pomodoro_todo.R
import com.chpham.pomodoro_todo.databinding.BottomSheetFragementAddTaskBinding
import com.chpham.pomodoro_todo.databinding.LayoutDatePickerBinding
import com.chpham.pomodoro_todo.databinding.LayoutRepeatOptionsBinding
import com.chpham.pomodoro_todo.databinding.LayoutTimePickerBinding
import com.chpham.pomodoro_todo.utils.Constants
import com.chpham.pomodoro_todo.utils.toDayMonthYearString
import com.chpham.pomodoro_todo.utils.toHourMinuteString
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class CreateTaskBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetFragementAddTaskBinding
    private lateinit var layoutDatePickerBinding: LayoutDatePickerBinding
    private lateinit var layoutTimePickerBinding: LayoutTimePickerBinding
    private lateinit var layoutRepeatOptionsBinding: LayoutRepeatOptionsBinding

    private var taskName: String = Constants.EMPTY
    private var taskDescription: String? = null

    private var selectedDate: Long = Calendar.getInstance().timeInMillis
    private var selectedTime: Long? = null

    private var selectedRemindBefore: Int? = null

    private var selectedCategory: String? = null

    private var selectedPriority: TaskPriority? = null

    private var selectedTag: Tag? = null

    private var selectedMode: RemindOptions.RemindMode? = null
    private var selectedInterval: Int = -1
    private var selectedRepeatIn: Int = -1
    private var selectedEndInt: Int = -1

    private var isTimeAllowed = false

    companion object {
        const val TAG = "CreateTaskBottomSheetDialogFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetFragementAddTaskBinding.inflate(inflater, container, false)
        layoutDatePickerBinding = LayoutDatePickerBinding.inflate(LayoutInflater.from(context))
        layoutTimePickerBinding = LayoutTimePickerBinding.inflate(LayoutInflater.from(context))
        layoutRepeatOptionsBinding =
            LayoutRepeatOptionsBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val datePickerDialogView = layoutDatePickerBinding.root
        val datePickerAlertDialogBuilder = AlertDialog.Builder(context ?: return)
        datePickerAlertDialogBuilder.setView(datePickerDialogView)
        val datePickerAlertDialog = datePickerAlertDialogBuilder.create()
        datePickerAlertDialog.setCanceledOnTouchOutside(false)

        val timePickerDialogView = layoutTimePickerBinding.root
        val timePickerAlertDialogBuilder = AlertDialog.Builder(context ?: return)
        timePickerAlertDialogBuilder.setView(timePickerDialogView)
        val timePickerAlertDialog = timePickerAlertDialogBuilder.create()
        timePickerAlertDialog.setCanceledOnTouchOutside(false)

        val setReminderDialogView = layoutRepeatOptionsBinding.root
        val setReminderAlertDialogBuilder = AlertDialog.Builder(context ?: return)
        setReminderAlertDialogBuilder.setView(setReminderDialogView)
        val setReminderAlertDialog = setReminderAlertDialogBuilder.create()
        setReminderAlertDialog.setCanceledOnTouchOutside(false)

        binding.btnDay.setOnClickListener {
            context?.let {
                val today = Calendar.getInstance().timeInMillis
                layoutDatePickerBinding.datePicker.minDate = today

                datePickerAlertDialog.show()

                layoutDatePickerBinding.datePicker.setOnDateChangeListener { _, year, month, dayOfMonth ->
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.set(year, month, dayOfMonth)
                    selectedDate = selectedCalendar.timeInMillis

                    val todayCalendar = Calendar.getInstance()

                    val tomorrowCalendar = Calendar.getInstance()
                    tomorrowCalendar.add(Calendar.DAY_OF_MONTH, 1)

                    when (selectedCalendar) {
                        todayCalendar -> {
                            binding.btnDay.text = getText(R.string.text_today)
                        }
                        tomorrowCalendar -> {
                            binding.btnDay.text = getText(R.string.text_tomorrow)
                        }
                        else -> {
                            val selectedDate = "$dayOfMonth/${month + 1}/$year"
                            binding.btnDay.text = selectedDate
                        }
                    }

                }

                layoutDatePickerBinding.tvTime.setOnClickListener {
                    timePickerAlertDialog.show()
                    var timeInMillis: Long = System.currentTimeMillis()
                    var remindBefore = 0
                    layoutTimePickerBinding.timerPicker.setOnTimeChangedListener { view, hourOfDay, minute ->
                        val calendar =
                            Calendar.getInstance() // create a Calendar object with the current date and time
                        calendar.set(
                            Calendar.HOUR_OF_DAY,
                            hourOfDay
                        ) // set the hour field to the selected hour
                        calendar.set(
                            Calendar.MINUTE,
                            minute
                        ) // set the minute field to the selected minute
                        calendar.set(
                            Calendar.SECOND,
                            0
                        ) // set the second field to 0 to avoid rounding issues

                        timeInMillis = calendar.timeInMillis // get the time in milliseconds

                        Log.e(
                            "ChienNgan",
                            "setOnTimeChangedListener: timeInMillis=$timeInMillis  format= ${timeInMillis.toHourMinuteString()}"
                        )

                        isTimeAllowed = if (timeInMillis < System.currentTimeMillis()) {
                            Toast.makeText(
                                context,
                                "Selected time is in the past",
                                Toast.LENGTH_SHORT
                            ).show()
                            false
                        } else {
                            true
                        }
                    }

                    layoutTimePickerBinding.tvNone.setOnClickListener {
                        unSelectPreviousOptions(remindBefore)
                        layoutTimePickerBinding.tvReminderValue.text =
                            layoutTimePickerBinding.tvNone.text
                        layoutTimePickerBinding.tvNone.setBackgroundResource(R.drawable.bg_btn_date_selected)
                        remindBefore = 0
                    }

                    layoutTimePickerBinding.tv5Minutes.setOnClickListener {
                        unSelectPreviousOptions(remindBefore)
                        layoutTimePickerBinding.tvReminderValue.text =
                            layoutTimePickerBinding.tv5Minutes.text
                        layoutTimePickerBinding.tv5Minutes.setBackgroundResource(R.drawable.bg_btn_date_selected)
                        remindBefore = 5
                    }

                    layoutTimePickerBinding.tv10Minutes.setOnClickListener {
                        unSelectPreviousOptions(remindBefore)
                        layoutTimePickerBinding.tvReminderValue.text =
                            layoutTimePickerBinding.tv10Minutes.text
                        layoutTimePickerBinding.tv10Minutes.setBackgroundResource(R.drawable.bg_btn_date_selected)
                        remindBefore = 10
                    }

                    layoutTimePickerBinding.tv15Minutes.setOnClickListener {
                        unSelectPreviousOptions(remindBefore)
                        layoutTimePickerBinding.tvReminderValue.text =
                            layoutTimePickerBinding.tv15Minutes.text
                        layoutTimePickerBinding.tv15Minutes.setBackgroundResource(R.drawable.bg_btn_date_selected)
                        remindBefore = 15
                    }

                    layoutTimePickerBinding.tv30Minutes.setOnClickListener {
                        unSelectPreviousOptions(remindBefore)
                        layoutTimePickerBinding.tvReminderValue.text =
                            layoutTimePickerBinding.tv30Minutes.text
                        layoutTimePickerBinding.tv30Minutes.setBackgroundResource(R.drawable.bg_btn_date_selected)
                        remindBefore = 30
                    }

                    layoutTimePickerBinding.tv1Hour.setOnClickListener {
                        unSelectPreviousOptions(remindBefore)
                        layoutTimePickerBinding.tvReminderValue.text =
                            layoutTimePickerBinding.tv1Hour.text
                        layoutTimePickerBinding.tv1Hour.setBackgroundResource(R.drawable.bg_btn_date_selected)
                        remindBefore = 60
                    }

                    layoutTimePickerBinding.tv2Hours.setOnClickListener {
                        unSelectPreviousOptions(remindBefore)
                        layoutTimePickerBinding.tvReminderValue.text =
                            layoutTimePickerBinding.tv2Hours.text
                        layoutTimePickerBinding.tv2Hours.setBackgroundResource(R.drawable.bg_btn_date_selected)
                        remindBefore = 120
                    }

                    layoutTimePickerBinding.tv3Hours.setOnClickListener {
                        unSelectPreviousOptions(remindBefore)
                        layoutTimePickerBinding.tvReminderValue.text =
                            layoutTimePickerBinding.tv3Hours.text
                        layoutTimePickerBinding.tv3Hours.setBackgroundResource(R.drawable.bg_btn_date_selected)
                        remindBefore = 180
                    }



                    layoutTimePickerBinding.btnCancel.setOnClickListener {
                        timePickerAlertDialog.dismiss()
                    }

                    layoutTimePickerBinding.btnConfirm.setOnClickListener {
                        if (isTimeAllowed) {
                            selectedTime = timeInMillis
                            selectedRemindBefore = remindBefore
                        }
                    }
                }

                layoutDatePickerBinding.tvRepeat.setOnClickListener {
                    setReminderAlertDialog.show()

                    layoutRepeatOptionsBinding.btnCancel.setOnClickListener {
                        setReminderAlertDialog.dismiss()
                    }
                }

                layoutDatePickerBinding.btnCancel.setOnClickListener {
                    datePickerAlertDialog.dismiss()
                }

                layoutDatePickerBinding.btnConfirm.setOnClickListener {
                    Log.e(
                        "ChienNgan",
                        "Date: $selectedDate = ${selectedDate.toDayMonthYearString()}"
                    )
                }

            }
        }

        binding.btnCategory.setOnClickListener {
            Toast.makeText(context, "Select category", Toast.LENGTH_SHORT).show()
        }

        binding.btnPriority.setOnClickListener {
            Toast.makeText(context, "Select Priority", Toast.LENGTH_SHORT).show()
        }

        binding.btnReminder.setOnClickListener {
            Toast.makeText(context, "Select Reminder", Toast.LENGTH_SHORT).show()
        }
    }

    private fun unSelectPreviousOptions(remindBefore: Int) {
        when (remindBefore) {
            0 -> {
                layoutTimePickerBinding.tvNone.setBackgroundResource(R.drawable.bg_btn_date_unselected)
            }
            5 -> {
                layoutTimePickerBinding.tv5Minutes.setBackgroundResource(R.drawable.bg_btn_date_unselected)
            }
            10 -> {
                layoutTimePickerBinding.tv10Minutes.setBackgroundResource(R.drawable.bg_btn_date_unselected)
            }
            15 -> {
                layoutTimePickerBinding.tv15Minutes.setBackgroundResource(R.drawable.bg_btn_date_unselected)
            }
            30 -> {
                layoutTimePickerBinding.tv30Minutes.setBackgroundResource(R.drawable.bg_btn_date_unselected)
            }
            60 -> {
                layoutTimePickerBinding.tv1Hour.setBackgroundResource(R.drawable.bg_btn_date_unselected)
            }
            120 -> {
                layoutTimePickerBinding.tv2Hours.setBackgroundResource(R.drawable.bg_btn_date_unselected)
            }
            180 -> {
                layoutTimePickerBinding.tv3Hours.setBackgroundResource(R.drawable.bg_btn_date_unselected)
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        Log.e("ChienNgan", "CreateTaskBottomSheetDialogFragment: onDismiss")
    }

}