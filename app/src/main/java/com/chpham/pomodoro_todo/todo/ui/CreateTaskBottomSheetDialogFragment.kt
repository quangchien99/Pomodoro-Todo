package com.chpham.pomodoro_todo.todo.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.chpham.domain.model.RemindOptions
import com.chpham.domain.model.TaskPriority
import com.chpham.pomodoro_todo.R
import com.chpham.pomodoro_todo.databinding.BottomSheetFragementAddTaskBinding
import com.chpham.pomodoro_todo.databinding.LayoutDatePickerBinding
import com.chpham.pomodoro_todo.databinding.LayoutRepeatOptionsBinding
import com.chpham.pomodoro_todo.databinding.LayoutTimePickerBinding
import com.chpham.pomodoro_todo.todo.ui.adapter.CategorySpinnerAdapter
import com.chpham.pomodoro_todo.todo.ui.adapter.PrioritySpinnerAdapter
import com.chpham.pomodoro_todo.utils.Constants
import com.chpham.pomodoro_todo.utils.createAlertDialog
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

    private var selectedTime: Long = System.currentTimeMillis()
    private var selectedRemindBefore: Int = 0

    private var selectedCategory: String? = null

    private var selectedPriority: TaskPriority = TaskPriority.NO_PRIORITY

    private var selectedMode: RemindOptions.RemindMode = RemindOptions.RemindMode.UN_SPECIFIED
    private var selectedInterval: String? = null
    private var selectedRepeatIn: String? = null
    private var selectedEndInt: String? = null

    private var tempDate: Long = -1

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

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpDateAndRepetition()

        setUpCategory()

        setUpPriority()

        setUpReminder()

        binding.btnAddTask.setOnClickListener {
            Log.e(
                "ChienNgan",
                "Create task: category= $selectedCategory -- priority= $selectedPriority -- time= $selectedTime -- remind before= $selectedRemindBefore -- date= ${selectedDate.toDayMonthYearString()} -- mode = $selectedMode -- interval= $selectedInterval -- repeat in= $selectedRepeatIn -- endIn= $selectedEndInt"
            )
        }
    }

    private fun setUpDateAndRepetition() {
        val datePickerAlertDialog = layoutDatePickerBinding.createAlertDialog(context ?: return)

        val repeatOptionsDialog = RepeatOptionsDialog(context ?: return, layoutRepeatOptionsBinding)

        binding.btnDay.setOnClickListener {

            handleSelectDate(datePickerAlertDialog)

            layoutDatePickerBinding.tvRepeat.setOnClickListener {
                repeatOptionsDialog.handleSelectRepetition(
                    currentMode = selectedMode,
                    currentInterval = selectedInterval,
                    currentRepeatIn = selectedRepeatIn,
                    currentEndInt = selectedEndInt
                ) { mode, interval, repeatIn, endIn ->
                    selectedMode = mode
                    selectedInterval = interval
                    selectedRepeatIn = repeatIn
                    selectedEndInt = endIn
                }
            }

            layoutDatePickerBinding.btnCancel.setOnClickListener {
                updateSelectedDayText(
                    Calendar.getInstance().apply {
                        this.timeInMillis = selectedDate
                    }
                )
                datePickerAlertDialog.dismiss()
            }

            layoutDatePickerBinding.btnConfirm.setOnClickListener {
                selectedDate = tempDate
                datePickerAlertDialog.dismiss()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpCategory() {
        context?.let { ctx ->
            val items =
                mutableListOf("No Category", "Work", "Personal", "Favorite", "Create new category")
            val categorySpinnerAdapter = CategorySpinnerAdapter(ctx, items)
            binding.spinnerCategory.adapter = categorySpinnerAdapter

            var lastSelectedPosition = -1
            binding.spinnerCategory.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    // Manually invoke onItemSelected when the same item is selected again
                    val spinnerPosition = binding.spinnerCategory.selectedItemPosition
                    if (spinnerPosition == lastSelectedPosition && spinnerPosition == categorySpinnerAdapter.count - 1) {
                        showAddCategoryDialog(ctx, categorySpinnerAdapter)
                    }
                    lastSelectedPosition = spinnerPosition
                }
                false
            }

            // Spinner item selected listener
            binding.spinnerCategory.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem = items[position]
                    if (selectedItem == "Create new category") {
                        showAddCategoryDialog(ctx, categorySpinnerAdapter)
                    }
                    selectedCategory = selectedItem
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }

    private fun setUpPriority() {
        val priorityValues = resources.getStringArray(R.array.priority_values)
        val priorityResources = arrayOf(
            R.drawable.ic_none_priority,
            R.drawable.ic_priority_low,
            R.drawable.ic_priority_medium,
            R.drawable.ic_priority_high,
        )
        val adapterPrioritySpinner = context?.let {
            PrioritySpinnerAdapter(
                context = it, objects = priorityValues, imageArray = priorityResources
            )
        }
        binding.spinnerPriority.adapter = adapterPrioritySpinner
        binding.spinnerPriority.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(adapter: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                // implement later
                selectedPriority = when (pos) {
                    1 -> {
                        TaskPriority.LOW
                    }
                    2 -> {
                        TaskPriority.MEDIUM
                    }
                    3 -> {
                        TaskPriority.HIGH
                    }
                    else -> {
                        TaskPriority.NO_PRIORITY
                    }
                }
            }

            override fun onNothingSelected(adapter: AdapterView<*>?) {
                // do nothing
            }
        }
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    private fun setUpReminder() {
        val timePickerAlertDialog = layoutTimePickerBinding.createAlertDialog(context ?: return)
        binding.btnReminder.setOnClickListener {
            timePickerAlertDialog.show()

            if (selectedRemindBefore == 0) {
                layoutTimePickerBinding.switchReminder.isChecked = false
            }

            var timeInMillis: Long = System.currentTimeMillis()
            val calendar = Calendar.getInstance()

            layoutTimePickerBinding.timerPicker.setOnTimeChangedListener { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                calendar.set(Calendar.SECOND, 0)
                timeInMillis = calendar.timeInMillis

                selectedTime = timeInMillis
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

                layoutTimePickerBinding.switchReminder.setOnClickListener {
                    if (layoutTimePickerBinding.switchReminder.isChecked) {
                        tv5Minutes.performClick()
                    } else {
                        tvNone.performClick()
                    }
                }

                btnCancel.setOnClickListener {
                    timePickerAlertDialog?.dismiss()
                }

                btnConfirm.setOnClickListener {
                    if (layoutTimePickerBinding.switchReminder.isChecked) {
                        binding.btnReminder.text = resources.getString(
                            R.string.text_remind_before_format,
                            if (selectedRemindBefore < 60) {
                                selectedRemindBefore.toString()
                            } else {
                                (selectedRemindBefore / 60).toString()
                            },
                            if (selectedRemindBefore < 60) {
                                getString(R.string.text_minutes)
                            } else if (selectedRemindBefore == 60) {
                                getString(R.string.text_hour)
                            } else {
                                getString(R.string.text_hours)
                            }
                        )
                        binding.btnReminder.setCompoundDrawablesWithIntrinsicBounds(
                            resources.getDrawable(
                                R.drawable.ic_alarm_on, null
                            ),
                            null, null, null
                        )
                    } else {
                        binding.btnReminder.setCompoundDrawablesWithIntrinsicBounds(
                            resources.getDrawable(
                                R.drawable.ic_alarm, null
                            ),
                            null, null, null
                        )
                        binding.btnReminder.text = resources.getString(R.string.text_reminder)
                    }
                    timePickerAlertDialog?.dismiss()
                }
            }
        }
    }

    private fun showAddCategoryDialog(
        ctx: Context,
        categorySpinnerAdapter: CategorySpinnerAdapter
    ) {
        // Show Add Item dialog
        val dialogView = LayoutInflater.from(ctx).inflate(R.layout.layout_add_category, null)
        val dialog = AlertDialog.Builder(ctx).setView(dialogView).create()

        // Add Button click listener
        val btnConfirm = dialogView.findViewById<TextView>(R.id.btnConfirm)
        val btnCancel = dialogView.findViewById<TextView>(R.id.btnCancel)
        btnConfirm.setOnClickListener {
            val categoryName =
                dialogView.findViewById<EditText>(R.id.edtCategoryName).text.toString()
            // Add new item to spinner
            categorySpinnerAdapter.addItem(categoryName)
            binding.spinnerCategory.setSelection(
                categorySpinnerAdapter.getPosition(
                    categoryName
                )
            )
            selectedCategory = categoryName
            dialog.dismiss()
        }
        btnCancel.setOnClickListener {
            selectedCategory = null
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun handleSelectDate(datePickerAlertDialog: AlertDialog?) {
        val today = Calendar.getInstance().timeInMillis
        layoutDatePickerBinding.datePicker.minDate = today
        layoutDatePickerBinding.datePicker.date = selectedDate

        datePickerAlertDialog?.show()

        layoutDatePickerBinding.datePicker.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(year, month, dayOfMonth)
            tempDate = selectedCalendar.timeInMillis

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
                // do nothing
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

    private fun setReminderClickListener(textView: TextView, minutes: Int) {
        textView.setOnClickListener {
            unSelectPreviousOptions(selectedRemindBefore)
            layoutTimePickerBinding.tvReminderValue.text = textView.text
            textView.setBackgroundResource(R.drawable.bg_btn_date_selected)
            selectedRemindBefore = minutes
            layoutTimePickerBinding.switchReminder.isChecked = minutes != 0
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        Log.e("ChienNgan", "CreateTaskBottomSheetDialogFragment: onDismiss")
    }
}
