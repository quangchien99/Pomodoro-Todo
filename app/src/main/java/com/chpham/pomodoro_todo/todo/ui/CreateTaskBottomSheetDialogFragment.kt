package com.chpham.pomodoro_todo.todo.ui

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.viewbinding.ViewBinding
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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Calendar

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

    private var tempMode = RemindOptions.RemindMode.UN_SPECIFIED
    private var temptInterval: String? = null
    private var tempRepeatIn: String? = null
    private var tempEndInt: String? = null

    private var tempRemindBefore = 0

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

        val datePickerAlertDialog = createAlertDialog(layoutDatePickerBinding, context)

        binding.btnDay.setOnClickListener {
            context?.let { cxt ->

                handleSelectDate(datePickerAlertDialog)

                handleSelectTime()

                handleSelectRepetition(cxt)

                layoutDatePickerBinding.btnCancel.setOnClickListener {
                    datePickerAlertDialog?.dismiss()
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

    private fun handleSelectRepetition(
        cxt: Context
    ) {

        val setReminderAlertDialog = createAlertDialog(layoutRepeatOptionsBinding, context)

        layoutDatePickerBinding.tvRepeat.setOnClickListener {
            setReminderAlertDialog?.show()

            val adapterIntervalDaily = createSpinnerAdapter(cxt, R.array.interval_daily)
            val adapterIntervalWeekly = createSpinnerAdapter(cxt, R.array.interval_weekly)
            val adapterIntervalMonthly = createSpinnerAdapter(cxt, R.array.interval_monthly)
            val adapterEndDaily = createSpinnerAdapter(cxt, R.array.end_daily)
            val adapterEndWeekly = createSpinnerAdapter(cxt, R.array.end_weekly)
            val adapterEndMonthly = createSpinnerAdapter(cxt, R.array.end_monthly)
            val adapterRepeatInMonthly =
                createSpinnerAdapter(cxt, R.array.repeat_in_monthly)

            layoutRepeatOptionsBinding.spinnerRepeatEvery.onItemSelectedListener =
                createItemSelectedListener { selectedItem ->
                    temptInterval = selectedItem
                }

            layoutRepeatOptionsBinding.spinnerRepeatEnd.onItemSelectedListener =
                createItemSelectedListener { selectedItem ->
                    tempEndInt = selectedItem
                }

            layoutRepeatOptionsBinding.spinnerRepeatIn.onItemSelectedListener =
                createItemSelectedListener { selectedItem ->
                    tempEndInt = selectedItem
                }

            layoutRepeatOptionsBinding.tvDaily.setOnClickListener {
                setRepeatOptions(
                    RemindOptions.RemindMode.DAILY,
                    adapterIntervalDaily,
                    adapterEndDaily,
                    null
                )
            }

            layoutRepeatOptionsBinding.tvWeekly.setOnClickListener {
                setRepeatOptions(
                    RemindOptions.RemindMode.WEEKLY,
                    adapterIntervalWeekly,
                    adapterEndWeekly,
                    null
                )
            }

            layoutRepeatOptionsBinding.tvMonthly.setOnClickListener {
                setRepeatOptions(
                    RemindOptions.RemindMode.MONTHLY,
                    adapterIntervalMonthly,
                    adapterEndMonthly,
                    adapterRepeatInMonthly
                )
            }

            layoutRepeatOptionsBinding.btnCancel.setOnClickListener {
                tempMode = RemindOptions.RemindMode.UN_SPECIFIED
                temptInterval = null
                tempRepeatIn = null
                tempEndInt = null
                setReminderAlertDialog?.dismiss()
            }

            layoutRepeatOptionsBinding.btnConfirm.setOnClickListener {
                Log.e(
                    "ChienNgan",
                    "mode = $tempMode interval= $temptInterval repeatIn= $tempRepeatIn endIn= $tempEndInt"
                )
            }
        }
    }

    private fun handleSelectDate(datePickerAlertDialog: AlertDialog?) {
        val today = Calendar.getInstance().timeInMillis
        layoutDatePickerBinding.datePicker.minDate = today

        datePickerAlertDialog?.show()

        layoutDatePickerBinding.datePicker.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(year, month, dayOfMonth)
            selectedDate = selectedCalendar.timeInMillis

            updateSelectedDayText(selectedCalendar)
        }
    }

    private fun updateSelectedDayText(selectedCalendar: Calendar) {
        val todayCalendar = Calendar.getInstance()
        val tomorrowCalendar = Calendar.getInstance()
        tomorrowCalendar.add(Calendar.DAY_OF_MONTH, 1)

        when (selectedCalendar) {
            todayCalendar -> {
                binding.btnDay.text = binding.root.context.getText(R.string.text_today)
            }
            tomorrowCalendar -> {
                binding.btnDay.text = binding.root.context.getText(R.string.text_tomorrow)
            }
            else -> {
                val selectedDate =
                    "${selectedCalendar.get(Calendar.DAY_OF_MONTH)}/${selectedCalendar.get(Calendar.MONTH) + 1}/${
                        selectedCalendar.get(Calendar.YEAR)
                    }"
                binding.btnDay.text = selectedDate
            }
        }
    }

    private fun handleSelectTime() {
        val timePickerAlertDialog = createAlertDialog(layoutTimePickerBinding, context)

        layoutDatePickerBinding.tvTime.setOnClickListener {
            timePickerAlertDialog?.show()
            var timeInMillis: Long = System.currentTimeMillis()
            val calendar = Calendar.getInstance()
            layoutTimePickerBinding.timerPicker.setOnTimeChangedListener { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                calendar.set(Calendar.SECOND, 0)
                timeInMillis = calendar.timeInMillis

                isTimeAllowed = timeInMillis >= System.currentTimeMillis()
                if (!isTimeAllowed) {
                    Toast.makeText(
                        context,
                        "Selected time is in the past",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            with(layoutTimePickerBinding) {
                setReminderClickListener(tvNone, 0)
                setReminderClickListener(tv5Minutes, 5)
                setReminderClickListener(tv10Minutes, 10)
                setReminderClickListener(tv15Minutes, 15)
                setReminderClickListener(tv30Minutes, 30)
                setReminderClickListener(tv1Hour, 60)
                setReminderClickListener(tv2Hours, 120)
                setReminderClickListener(tv3Hours, 180)

                btnCancel.setOnClickListener {
                    tempRemindBefore = 0
                    timePickerAlertDialog?.dismiss()
                }

                btnConfirm.setOnClickListener {
                    if (isTimeAllowed) {
                        selectedTime = timeInMillis
                        selectedRemindBefore = tempRemindBefore
                    }
                }
            }

        }
    }

    private fun unSelectPreviousMode(mode: RemindOptions.RemindMode) {
        when (mode) {
            RemindOptions.RemindMode.DAILY -> {
                layoutRepeatOptionsBinding.tvDaily.setBackgroundResource(R.drawable.bg_btn_date_unselected)
            }
            RemindOptions.RemindMode.WEEKLY -> {
                layoutRepeatOptionsBinding.tvWeekly.setBackgroundResource(R.drawable.bg_btn_date_unselected)
            }
            RemindOptions.RemindMode.MONTHLY -> {
                layoutRepeatOptionsBinding.tvMonthly.setBackgroundResource(R.drawable.bg_btn_date_unselected)
            }
            else -> {
                //do nothing
            }
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

    private fun setRepeatOptions(
        mode: RemindOptions.RemindMode,
        intervalAdapter: ArrayAdapter<CharSequence>,
        endAdapter: ArrayAdapter<CharSequence>,
        inAdapter: ArrayAdapter<CharSequence>?
    ) {
        unSelectPreviousMode(tempMode)

        layoutRepeatOptionsBinding.layoutRepeatEvery.visibility = View.VISIBLE
        layoutRepeatOptionsBinding.spinnerRepeatEvery.adapter = intervalAdapter

        layoutRepeatOptionsBinding.layoutRepeatEndIn.visibility = View.VISIBLE
        layoutRepeatOptionsBinding.spinnerRepeatEnd.adapter = endAdapter

        inAdapter?.let {
            layoutRepeatOptionsBinding.layoutRepeatInMonth.visibility = View.VISIBLE
            layoutRepeatOptionsBinding.spinnerRepeatIn.adapter = it
        } ?: run {
            layoutRepeatOptionsBinding.layoutRepeatInMonth.visibility = View.GONE
        }

        layoutRepeatOptionsBinding.layoutRepeatInWeek.visibility = View.GONE

        tempMode = mode
        when (mode) {
            RemindOptions.RemindMode.DAILY -> layoutRepeatOptionsBinding.tvDaily.setBackgroundResource(
                R.drawable.bg_btn_date_selected
            )
            RemindOptions.RemindMode.WEEKLY -> {
                layoutRepeatOptionsBinding.tvWeekly.setBackgroundResource(R.drawable.bg_btn_date_selected)

                layoutRepeatOptionsBinding.layoutRepeatInWeek.visibility = View.VISIBLE

                val tvMappings = mapOf(
                    getString(R.string.text_mon) to layoutRepeatOptionsBinding.tvMonday,
                    getString(R.string.text_tue) to layoutRepeatOptionsBinding.tvTuesday,
                    getString(R.string.text_wed) to layoutRepeatOptionsBinding.tvWednesday,
                    getString(R.string.text_thu) to layoutRepeatOptionsBinding.tvThursday,
                    getString(R.string.text_fri) to layoutRepeatOptionsBinding.tvFriday,
                    getString(R.string.text_sat) to layoutRepeatOptionsBinding.tvSaturday,
                    getString(R.string.text_sun) to layoutRepeatOptionsBinding.tvSunday
                )

                val tvDaysInWeek = listOf(
                    layoutRepeatOptionsBinding.tvMonday,
                    layoutRepeatOptionsBinding.tvTuesday,
                    layoutRepeatOptionsBinding.tvWednesday,
                    layoutRepeatOptionsBinding.tvThursday,
                    layoutRepeatOptionsBinding.tvFriday,
                    layoutRepeatOptionsBinding.tvSaturday,
                    layoutRepeatOptionsBinding.tvSunday
                )

                for (tvDay in tvDaysInWeek) {
                    tvDay.setOnClickListener {
                        updateStateItemDayOfWeek(tvMappings)
                        tempRepeatIn = tvDay.text.toString()
                        tvDay.setBackgroundResource(R.drawable.bg_btn_date_selected)
                    }
                }
            }
            RemindOptions.RemindMode.MONTHLY -> layoutRepeatOptionsBinding.tvMonthly.setBackgroundResource(
                R.drawable.bg_btn_date_selected
            )
            else -> {
                //do nothing
            }
        }
    }

    private fun updateStateItemDayOfWeek(tvMappings: Map<String, TextView>) {
        tvMappings[tempRepeatIn]?.setBackgroundResource(R.drawable.bg_btn_date_unselected)
    }

    private fun createSpinnerAdapter(cxt: Context, arrayResId: Int): ArrayAdapter<CharSequence> {
        val adapter = ArrayAdapter.createFromResource(
            cxt,
            arrayResId,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return adapter
    }

    private fun createItemSelectedListener(itemCallback: (String) -> Unit): AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(pos).toString()
                itemCallback(selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // do nothing here
            }
        }
    }

    private fun createAlertDialog(
        binding: ViewBinding,
        context: Context?
    ): AlertDialog? {
        val dialogView = binding.root
        val alertDialogBuilder = AlertDialog.Builder(context ?: return null)
        alertDialogBuilder.setView(dialogView)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        return alertDialog
    }

    private fun setReminderClickListener(textView: TextView, minutes: Int) {
        textView.setOnClickListener {
            unSelectPreviousOptions(tempRemindBefore)
            layoutTimePickerBinding.tvReminderValue.text = textView.text
            textView.setBackgroundResource(R.drawable.bg_btn_date_selected)
            tempRemindBefore = minutes
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        Log.e("ChienNgan", "CreateTaskBottomSheetDialogFragment: onDismiss")
    }

}