package com.chpham.pomodoro_todo.todo.ui.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import com.chpham.domain.model.RemindOptions
import com.chpham.domain.model.Task
import com.chpham.domain.model.TaskPriority
import com.chpham.pomodoro_todo.R
import com.chpham.pomodoro_todo.databinding.BottomSheetFragementAddTaskBinding
import com.chpham.pomodoro_todo.databinding.LayoutDatePickerBinding
import com.chpham.pomodoro_todo.databinding.LayoutRepeatOptionsBinding
import com.chpham.pomodoro_todo.databinding.LayoutTimePickerBinding
import com.chpham.pomodoro_todo.todo.ui.adapter.CategorySpinnerAdapter
import com.chpham.pomodoro_todo.todo.ui.adapter.PrioritySpinnerAdapter
import com.chpham.pomodoro_todo.todo.viewmodel.TodoListViewModel
import com.chpham.pomodoro_todo.utils.convertToDays
import com.chpham.pomodoro_todo.utils.getDay
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Calendar

class CreateOrEditTaskBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetFragementAddTaskBinding
    private lateinit var layoutDatePickerBinding: LayoutDatePickerBinding
    private lateinit var layoutTimePickerBinding: LayoutTimePickerBinding
    private lateinit var layoutRepeatOptionsBinding: LayoutRepeatOptionsBinding

    private var isCreate: Boolean = true
    private var taskId: Int = -1
    private var task: Task? = null

    private var selectedDate: Long = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    private var selectedTime: Long = System.currentTimeMillis()
    private var selectedRemindBefore: Int = 0

    private var selectedCategory: String? = null

    private var selectedPriority: TaskPriority = TaskPriority.NO_PRIORITY

    private var selectedMode: RemindOptions.RemindMode = RemindOptions.RemindMode.UN_SPECIFIED
    private var selectedInterval: String? = null
    private var selectedRepeatIn: String? = null
    private val selectedRepeatInWeek: MutableList<String> = mutableListOf()
    private var selectedEndInt: String? = null

    // Get a reference to the parent fragment's view model
    private val todoListViewModel: TodoListViewModel by activityViewModels()

    companion object {
        const val TAG = "CreateTaskBottomSheetDialogFragment"
        private const val ARG_IS_CREATE = "is_create"
        private const val ARG_TASK_ID = "task_id"
        fun newInstance(
            isCreate: Boolean = true,
            taskId: Int = -1
        ): CreateOrEditTaskBottomSheetDialogFragment {
            val args = Bundle()
            args.putBoolean(ARG_IS_CREATE, isCreate)
            if (!isCreate) {
                args.putInt(ARG_TASK_ID, taskId)
            }
            val fragment = CreateOrEditTaskBottomSheetDialogFragment()
            fragment.arguments = args
            return fragment
        }
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

        isCreate = arguments?.getBoolean(ARG_IS_CREATE) == true
        if (isCreate) {
            binding.tvHeader.text = getString(R.string.text_add_task)
            initViews()
        } else {
            binding.tvHeader.text = getString(R.string.text_edit_task)
            taskId = arguments?.getInt(ARG_TASK_ID) ?: -1
            todoListViewModel.getTaskById(taskId).observe(viewLifecycleOwner) {
                handleDataEdit(it)
            }
        }

        binding.edtTaskName.doOnTextChanged { _, _, _, _ ->
            binding.tvError.visibility = View.GONE
        }
        binding.btnAddTask.setOnClickListener {
            if (binding.edtTaskName.text.isNullOrEmpty()) {
                binding.tvError.visibility = View.VISIBLE
            } else {
                val task = Task(
                    name = binding.edtTaskName.text.toString(),
                    timeCreated = System.currentTimeMillis(),
                    priority = selectedPriority,
                    dueDate = selectedDate,
                    category = if (selectedCategory == getString(R.string.text_no_category)) {
                        null
                    } else {
                        selectedCategory
                    },
                    deadline = selectedTime,
                    remindOptions = RemindOptions(
                        mode = selectedMode,
                        interval = selectedInterval?.convertToDays(
                            context ?: return@setOnClickListener
                        ) ?: -1,
                        repeatInMonth = selectedRepeatIn?.getDay() ?: -1,
                        repeatInWeek = selectedRepeatInWeek,
                        endInt = selectedEndInt?.convertToDays(context ?: return@setOnClickListener)
                            ?: -1
                    ),
                    remindBefore = selectedRemindBefore,
                    description = binding.edtTaskDescription.text.toString()
                )
                if (isCreate) {
                    todoListViewModel.insertTask(task)
                } else {
                    todoListViewModel.updateTask(
                        task = task.copy(
                            id = taskId
                        )
                    ).also {
                        this.dismiss()
                    }
                }
            }
        }
    }

    private fun handleDataEdit(task: Task?) {
        this.task = task
        this.task?.let { editingTask ->
            binding.edtTaskName.text.append(editingTask.name)

            binding.edtTaskDescription.text.append(editingTask.description)

            selectedDate = editingTask.dueDate

            editingTask.deadline?.let { deadline ->
                selectedTime = deadline
            }
            editingTask.remindBefore?.let { remindBefore ->
                selectedRemindBefore = remindBefore
            }

            editingTask.category?.let { category ->
                selectedCategory = category
            }

            selectedPriority = editingTask.priority

            editingTask.remindOptions?.let { remindOptions ->
                selectedMode = remindOptions.mode
                context?.let { ctx ->
                    when (remindOptions.mode) {
                        RemindOptions.RemindMode.DAILY -> {
                            val intervalValues =
                                ctx.resources.getStringArray(R.array.interval_daily)

                            val endValues = ctx.resources.getStringArray(R.array.end_daily)

                            updateSelectedIntervalAndEndInt(
                                intervalValues,
                                endValues,
                                remindOptions.interval,
                                remindOptions.endInt,
                                ctx
                            )
                        }
                        RemindOptions.RemindMode.WEEKLY -> {
                            val intervalValues =
                                ctx.resources.getStringArray(R.array.interval_weekly)

                            val endValues = ctx.resources.getStringArray(R.array.end_weekly)

                            updateSelectedIntervalAndEndInt(
                                intervalValues,
                                endValues,
                                remindOptions.interval,
                                remindOptions.endInt,
                                ctx
                            )

                            editingTask.remindOptions?.repeatInWeek?.let { repeatInWeek ->
                                selectedRepeatInWeek.clear()
                                selectedRepeatInWeek.addAll(repeatInWeek)
                            }
                        }
                        RemindOptions.RemindMode.MONTHLY -> {
                            val intervalValues =
                                ctx.resources.getStringArray(R.array.interval_monthly)

                            val endValues = ctx.resources.getStringArray(R.array.end_monthly)

                            updateSelectedIntervalAndEndInt(
                                intervalValues,
                                endValues,
                                remindOptions.interval,
                                remindOptions.endInt,
                                ctx
                            )

                            val repeatInMonthlyValues =
                                ctx.resources.getStringArray(R.array.repeat_in_monthly)
                            repeatInMonthlyValues.let { repeatInMonthly ->
                                repeatInMonthly.forEach { repeatIn ->
                                    if (repeatIn.getDay() == editingTask.remindOptions?.repeatInMonth) {
                                        selectedRepeatIn = repeatIn
                                    }
                                }
                            }
                        }
                        else -> {
                            // do nothing
                        }
                    }
                }
            }

            initViews(true)
        }
    }

    private fun updateSelectedIntervalAndEndInt(
        intervalValues: Array<String>,
        endValues: Array<String>,
        interval: Int?,
        endInt: Int?,
        ctx: Context
    ) {
        intervalValues.let { intervals ->
            intervals.forEach {
                if (it.convertToDays(ctx) == interval) {
                    selectedInterval = it
                }
            }
        }

        endValues.let { ends ->
            ends.forEach { end ->
                if (end.convertToDays(ctx) == endInt) {
                    selectedEndInt = end
                }
            }
        }
    }

    private fun initViews(isEdit: Boolean = false) {
        setUpDateAndRepetition(isEdit)

        setUpCategory(isEdit)

        setUpPriority(isEdit)

        setUpReminder(isEdit)
    }

    private fun setUpDateAndRepetition(isEdit: Boolean = false) {

        val datePickerDialog =
            DatePickerDialog(
                context ?: return,
                layoutDatePickerBinding,
                layoutRepeatOptionsBinding
            )
        if (isEdit) {
            updateSelectedDayText(
                Calendar.getInstance().apply {
                    this.timeInMillis = selectedDate
                }
            )
        }

        binding.btnDay.setOnClickListener {

            datePickerDialog.handleSelectDate(
                currentDate = selectedDate,
                currentMode = selectedMode,
                currentInterval = selectedInterval,
                currentRepeatIn = selectedRepeatIn,
                currentRepeatInWeek = selectedRepeatInWeek,
                currentEndInt = selectedEndInt
            ) { date, mode, interval, repeatIn, repeatInWeek, endIn ->
                selectedDate = date
                selectedMode = mode
                selectedInterval = interval
                selectedRepeatIn = repeatIn
                selectedRepeatInWeek.clear()
                selectedRepeatInWeek.addAll(repeatInWeek)
                selectedEndInt = endIn

                updateSelectedDayText(
                    Calendar.getInstance().apply {
                        this.timeInMillis = selectedDate
                    }
                )
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpCategory(isEdit: Boolean = false) {
        context?.let { ctx ->
            val items =
                mutableListOf("No Category", "Work", "Personal", "Favorite", "Create new category")
            val categorySpinnerAdapter = CategorySpinnerAdapter(ctx, items)
            binding.spinnerCategory.adapter = categorySpinnerAdapter

            var lastSelectedPosition = -1

            if (isEdit) {
                selectedCategory?.let { category ->
                    binding.spinnerCategory.setSelection(
                        items.indexOf(category).also { index ->
                            lastSelectedPosition = index
                        }
                    )
                }
            }

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

    private fun setUpPriority(isEdit: Boolean = false) {
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

        if (isEdit) {
            when (selectedPriority) {
                TaskPriority.LOW -> {
                    binding.spinnerPriority.setSelection(1)
                }
                TaskPriority.MEDIUM -> {
                    binding.spinnerPriority.setSelection(2)
                }
                TaskPriority.HIGH -> {
                    binding.spinnerPriority.setSelection(3)
                }
                else -> {
                    binding.spinnerPriority.setSelection(0)
                }
            }
        }
    }

    private fun setUpReminder(isEdit: Boolean = false) {
        val reminderDialog = ReminderDialog(context ?: return, layoutTimePickerBinding)
        if (isEdit) {
            setUpViewBtnReminder()
        }
        binding.btnReminder.setOnClickListener {
            reminderDialog.handleSetUpReminder(
                currentTime = selectedTime,
                currentRemindBefore = selectedRemindBefore
            ) { time, remindBefore ->
                selectedTime = time
                selectedRemindBefore = remindBefore
                setUpViewBtnReminder()
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setUpViewBtnReminder() {
        if (selectedRemindBefore > 0) {
            binding.btnReminder.setCompoundDrawablesWithIntrinsicBounds(
                resources.getDrawable(
                    R.drawable.ic_alarm_on,
                    null
                ),
                null, null, null
            )
            binding.btnReminder.text = getString(
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
        } else {
            binding.btnReminder.setCompoundDrawablesWithIntrinsicBounds(
                resources.getDrawable(
                    R.drawable.ic_alarm,
                    null
                ),
                null, null, null
            )
            binding.btnReminder.text = getString(R.string.text_reminder)
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
}
