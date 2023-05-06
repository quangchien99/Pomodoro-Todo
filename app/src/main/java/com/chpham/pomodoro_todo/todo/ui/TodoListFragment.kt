package com.chpham.pomodoro_todo.todo.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.chpham.domain.model.Task
import com.chpham.domain.model.TaskState
import com.chpham.pomodoro_todo.HomeActivity
import com.chpham.pomodoro_todo.R
import com.chpham.pomodoro_todo.base.ui.BaseFragment
import com.chpham.pomodoro_todo.base.viewmodel.ViewModelState
import com.chpham.pomodoro_todo.databinding.FragmentTodoBinding
import com.chpham.pomodoro_todo.todo.ui.adapter.CategoriesAdapter
import com.chpham.pomodoro_todo.todo.ui.adapter.TasksAdapter
import com.chpham.pomodoro_todo.todo.ui.adapter.TasksAndHeadersAdapter
import com.chpham.pomodoro_todo.todo.ui.adapter.swipe.DragAndSwipeCallback
import com.chpham.pomodoro_todo.todo.ui.dialog.CreateOrEditTaskBottomSheetDialogFragment
import com.chpham.pomodoro_todo.todo.viewmodel.TodoListViewModel
import com.chpham.pomodoro_todo.utils.Constants.HEADER_DONE
import com.chpham.pomodoro_todo.utils.Constants.HEADER_IN_PROGRESS
import com.chpham.pomodoro_todo.utils.Constants.HEADER_TODO
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.util.Collections

@AndroidEntryPoint
class TodoListFragment :
    BaseFragment<FragmentTodoBinding>(),
    DragAndSwipeCallback.ItemTouchHelperListener {

    companion object {
        fun newInstance(): TodoListFragment {
            return TodoListFragment()
        }
    }

    private lateinit var previousTasksAdapter: TasksAdapter
    private lateinit var todayTasksAdapter: TasksAndHeadersAdapter
    private lateinit var next7DaysTasksAdapter: TasksAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    private var bottomSheetDialogFragment: CreateOrEditTaskBottomSheetDialogFragment? = null

    private val todoListViewModel: TodoListViewModel by activityViewModels()

    private var dialog: AlertDialog? = null

    private var taskIdNeedToMarkDone = -1
    private val markTaskDone: (Boolean) -> () -> Unit = { isConfirmed ->
        {
            taskIdNeedToMarkDone = if (isConfirmed) {
                if (taskIdNeedToMarkDone != -1) {
                    todoListViewModel.setTaskState(taskIdNeedToMarkDone, TaskState.DONE)
                }
                -1
            } else {
                -1
            }
            dialog = null
        }
    }

    private var taskIdNeedToMarkInProgress = -1
    private val markTaskInProgress: (Boolean) -> () -> Unit = { isConfirmed ->
        {
            taskIdNeedToMarkInProgress = if (isConfirmed) {
                if (taskIdNeedToMarkInProgress != -1) {
                    todoListViewModel.setTaskState(
                        taskIdNeedToMarkInProgress, TaskState.IN_PROGRESS
                    )
                }
                -1
            } else {
                -1
            }
            dialog = null
        }
    }

    private var categoryFilter: String = "All"

    override fun initViewBinding(inflater: LayoutInflater, container: ViewGroup?): ViewBinding {
        return FragmentTodoBinding.inflate(layoutInflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        initCategoriesRecyclerView()
        initPreviousTasksRecyclerView()
        initTodayTasksRecyclerView()
        initNext7DaysTasksRecyclerView()
        initObservers()
        initData()
        initClickListener()
        binding.fabAddTask.setOnClickListener {
            bottomSheetDialogFragment = CreateOrEditTaskBottomSheetDialogFragment.newInstance(
                isCreate = true
            )
            activity?.supportFragmentManager?.let {
                bottomSheetDialogFragment?.show(it, CreateOrEditTaskBottomSheetDialogFragment.TAG)
            }
        }

        binding.imgMore.setOnClickListener {
            // later
        }
    }

    private fun initData() {
        todoListViewModel.getYesterdayTasks()
        todoListViewModel.getTodayTasks()
        todoListViewModel.getNext7DaysTasks()
    }

    private fun initCategoriesRecyclerView() {
        val categoryClickListener = object : CategoriesAdapter.CategoryClickListener {
            override fun onCategoryClick(category: String) {
                if (category != categoryFilter) {
                    categoryFilter = category
                    updateTaskBySelectedCategory()
                }
            }
        }

        categoriesAdapter = CategoriesAdapter(categoryClickListener)

        binding.rcvCategories.apply {
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
            adapter = categoriesAdapter
        }

        binding.rcvCategories.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    private fun updateTaskBySelectedCategory() {
        displayTodayTasks(todoListViewModel.todayTasks.value)
    }

    private fun initPreviousTasksRecyclerView() {
        val previousTaskClickListener = object : TasksAdapter.TaskClickListener {
            override fun onTaskClick(taskId: Int, card: CardView) {
                Log.e("ChienNgan", "previousTaskClickListener: onTaskClick")
                TODO("Not yet implemented")
            }

            override fun onTaskDoneClick(task: Task) {
                Log.e("ChienNgan", "previousTaskClickListener: onTaskDoneClick")
                TODO("Not yet implemented")
            }

            override fun onTaskDoingClick(task: Task) {
                Log.e("ChienNgan", "previousTaskClickListener: onTaskDoingClick")
                TODO("Not yet implemented")
            }
        }
        previousTasksAdapter = TasksAdapter(previousTaskClickListener)

        binding.rcvPreviousTasks.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = previousTasksAdapter
        }

        binding.rcvPreviousTasks.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    private fun initTodayTasksRecyclerView() {
        val todayTaskClickListener = object : TasksAndHeadersAdapter.TaskClickListener {
            override fun onTaskDoneClick(taskId: Int) {
                todoListViewModel.setTaskState(taskId, TaskState.DONE)
            }

            override fun onEditTaskClicked(
                holder: TasksAndHeadersAdapter.TaskViewHolder,
                task: Task
            ) {
                val bottomSheetDialogFragment =
                    CreateOrEditTaskBottomSheetDialogFragment.newInstance(
                        isCreate = false,
                        taskId = task.id
                    )
                activity?.supportFragmentManager?.let {
                    bottomSheetDialogFragment.show(
                        it,
                        CreateOrEditTaskBottomSheetDialogFragment.TAG
                    )
                }
            }

            override fun onRemoveTaskClicked(
                holder: TasksAndHeadersAdapter.TaskViewHolder,
                task: Task
            ) {
                context?.let {
                    MaterialAlertDialogBuilder(it).setCancelable(false).setTitle("Delete task")
                        .setMessage(
                            "Are you sure profile '${task.name}' will be permanently deleted?"
                        ).setNegativeButton(android.R.string.cancel) { d, _ ->
                            d.dismiss()
                            holder.resetView(animated = true)
                        }.setPositiveButton(android.R.string.ok) { d, _ ->
                            d.dismiss()
                            todoListViewModel.deleteTask(task.id)
                        }.show()
                }
            }
        }
        context?.let {
            todayTasksAdapter = TasksAndHeadersAdapter(it, todayTaskClickListener)
            binding.rcvTodayTasks.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = todayTasksAdapter
            }

            binding.rcvTodayTasks.doOnPreDraw {
                startPostponedEnterTransition()
            }
        }
        ItemTouchHelper(DragAndSwipeCallback(this)).attachToRecyclerView(binding.rcvTodayTasks)
    }

    private fun initNext7DaysTasksRecyclerView() {
        val next7DaysTaskClickListener = object : TasksAdapter.TaskClickListener {
            override fun onTaskClick(taskId: Int, card: CardView) {
                Log.e("ChienNgan", "next7DaysTaskClickListener: onTaskClick")
                TODO("Not yet implemented")
            }

            override fun onTaskDoneClick(task: Task) {
                Log.e("ChienNgan", "next7DaysTaskClickListener: onTaskDoneClick")
                TODO("Not yet implemented")
            }

            override fun onTaskDoingClick(task: Task) {
                Log.e("ChienNgan", "next7DaysTaskClickListener: onTaskDoingClick")
                TODO("Not yet implemented")
            }
        }
        next7DaysTasksAdapter = TasksAdapter(next7DaysTaskClickListener)

        binding.rcvNext7DaysTasks.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = next7DaysTasksAdapter
        }

        binding.rcvNext7DaysTasks.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    private fun initObservers() {

        todoListViewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                ViewModelState.INSERTING -> {
                    bottomSheetDialogFragment?.dismiss()
                    bottomSheetDialogFragment?.onDestroy()
                }
                is ViewModelState.InsertSucceeded -> {
                    todoListViewModel.getTaskById(it.id) { taskOrNull ->
                        taskOrNull?.let { task ->
                            if (task.deadline != null && task.deadline!! > 0 && task.remindBefore != null && task.remindBefore!! > 0) {
                                todoListViewModel.createAlarm(
                                    id = task.id,
                                    remindTime = task.deadline!! - task.remindBefore!! * 60_000,
                                    message = task.name,
                                    startDate = task.dueDate,
                                    remindOptions = task.remindOptions
                                )
                            }
                        }
                    }
                }
                is ViewModelState.UpdateSucceeded -> {
                    todoListViewModel.getTaskById(it.id) { taskOrNull ->
                        taskOrNull?.let { task ->
                            if (task.deadline != null && task.deadline!! > 0 && task.remindBefore != null && task.remindBefore!! > 0) {
                                todoListViewModel.updateAlarm(
                                    id = task.id,
                                    remindTime = task.deadline!! - task.remindBefore!! * 60_000,
                                    message = task.name,
                                    startDate = task.dueDate,
                                    remindOptions = task.remindOptions
                                )
                            }
                        }
                    }
                }
                else -> {
                    // do nothing yet
                }
            }
        }
        todoListViewModel.yesterdayTasks.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.tvPrevious.visibility = View.GONE
            } else {
                binding.tvPrevious.visibility = View.VISIBLE
            }
            previousTasksAdapter.differ.submitList(it)
        }

        observeTodayTasks()

        todoListViewModel.next7DaysTasks.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.tvFuture.visibility = View.GONE
            } else {
                binding.tvFuture.visibility = View.VISIBLE
            }
            next7DaysTasksAdapter.differ.submitList(it)
        }

        todoListViewModel.categories.observe(viewLifecycleOwner) { categories ->
            categoriesAdapter.differ.submitList(categories)
        }.also {
            todoListViewModel.getCategories()
        }
    }

    private fun observeTodayTasks() {
        todoListViewModel.todayTasks.observe(viewLifecycleOwner) { tasks ->
            displayTodayTasks(tasks)
        }
    }

    private fun displayTodayTasks(tasks: List<Task?>?) {
        var displayTasks = tasks

        if (categoryFilter != "All") {
            displayTasks = tasks?.filter {
                it?.category == categoryFilter
            }
        }

        if (displayTasks.isNullOrEmpty()) {
            binding.tvEmptyTasks.visibility = View.VISIBLE
            binding.animEmptyTasks.visibility = View.VISIBLE
        } else {
            binding.tvEmptyTasks.visibility = View.GONE
            binding.animEmptyTasks.visibility = View.GONE

            val todoTasks = displayTasks.filter { it?.state == TaskState.TO_DO }
            val inProgressTasks = displayTasks.filter { it?.state == TaskState.IN_PROGRESS }
            val doneTasks = displayTasks.filter { it?.state == TaskState.DONE }

            val tasksAndHeader = mutableListOf<Pair<Task?, String?>>()
            tasksAndHeader.add(Pair(null, HEADER_TODO))
            tasksAndHeader.addAll(todoTasks.map { Pair(it, null) })
            tasksAndHeader.add(Pair(null, HEADER_IN_PROGRESS))
            tasksAndHeader.addAll(inProgressTasks.map { Pair(it, null) })
            tasksAndHeader.add(Pair(null, HEADER_DONE))
            tasksAndHeader.addAll(doneTasks.map { Pair(it, null) })

            todayTasksAdapter.differ.submitList(tasksAndHeader)
        }
    }

    private fun initClickListener() {
        setHeaderClickListener(
            binding.layoutHeaderPrevious, binding.rcvPreviousTasks, binding.tvPrevious
        )

        setHeaderClickListener(
            binding.layoutHeaderToday, binding.rcvTodayTasks, binding.tvToday
        )

        setHeaderClickListener(
            binding.layoutHeaderFuture, binding.rcvNext7DaysTasks, binding.tvFuture
        )

        binding.scrollView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (scrollY > oldScrollY) {
                // User scrolled down
                (activity as? HomeActivity)?.setFocusStatus(shouldFocusing = true)
            } else if (scrollY < oldScrollY) {
                // User scrolled up
                (activity as? HomeActivity)?.setFocusStatus(shouldFocusing = false)
            }
        }
    }

    private fun setHeaderClickListener(
        layoutHeader: View,
        recyclerView: View,
        textView: TextView
    ) {
        layoutHeader.setOnClickListener {
            recyclerView.isVisible = !recyclerView.isVisible
            val drawable = if (recyclerView.isVisible) {
                R.drawable.ic_arrow_drop_down
            } else {
                R.drawable.ic_arrow_drop_up
            }
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0)
        }
    }

    private fun showDialog(
        title: String,
        message: String,
        positiveAction: () -> Unit,
        negativeAction: () -> Unit
    ) {
        if (dialog == null) {
            dialog = AlertDialog.Builder(requireContext()).setTitle(title).setMessage(message)
                .setPositiveButton("OK") { _, _ -> positiveAction.invoke() }
                .setNegativeButton("Cancel") { _, _ -> negativeAction.invoke() }.create()
        }
        dialog?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dialog?.dismiss()
        dialog = null
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        val items = todayTasksAdapter.differ.currentList.toMutableList()

        val headerTodoPosition = items.indexOfFirst { it.second == HEADER_TODO }
        val headerInProgressPosition =
            items.indexOfFirst { it.second == HEADER_IN_PROGRESS }
        val headerDonePosition = items.indexOfFirst { it.second == HEADER_DONE }
        if ((fromPosition in headerTodoPosition + 1 until headerInProgressPosition && toPosition in headerTodoPosition + 1 until headerInProgressPosition) || (fromPosition in headerInProgressPosition + 1 until headerDonePosition && toPosition in headerInProgressPosition + 1 until headerDonePosition) || (fromPosition > headerDonePosition && toPosition > headerDonePosition)) {
            Collections.swap(items, fromPosition, toPosition)
            todayTasksAdapter.differ.submitList(items)
            todayTasksAdapter.notifyItemRangeChanged(fromPosition, toPosition)
            return true
        } else if (fromPosition < headerInProgressPosition && toPosition in headerInProgressPosition until headerDonePosition) {
            todayTasksAdapter.differ.currentList[fromPosition].first?.id?.let {
                todoListViewModel.setTaskState(
                    it, TaskState.IN_PROGRESS
                )
            }
            return true
        } else if (fromPosition in headerInProgressPosition + 1 until headerDonePosition && toPosition <= headerInProgressPosition) {
            todayTasksAdapter.differ.currentList[fromPosition].first?.id?.let {
                todoListViewModel.setTaskState(
                    it, TaskState.TO_DO
                )
            }
            return true
        } else if (headerDonePosition in (fromPosition + 1)..toPosition) {
            taskIdNeedToMarkDone =
                todayTasksAdapter.differ.currentList[fromPosition].first?.id ?: -1
            showDialog(
                title = "Mark Task Done",
                message = "Are u sure to make it completed?",
                positiveAction = {
                    markTaskDone(true).invoke()
                    dialog?.dismiss()
                },
                negativeAction = {
                    markTaskDone(false).invoke()
                    dialog?.dismiss()
                }
            )
            return true
        } else if (fromPosition > headerDonePosition && toPosition in (headerTodoPosition + 1)..headerDonePosition) {
            taskIdNeedToMarkInProgress =
                todayTasksAdapter.differ.currentList[fromPosition].first?.id ?: -1
            showDialog(
                title = "Mark Task In Progress",
                message = "Do you want to change this task to In Progress?",
                positiveAction = {
                    markTaskInProgress(true).invoke()
                    dialog?.dismiss()
                },
                negativeAction = {
                    markTaskInProgress(false).invoke()
                    dialog?.dismiss()
                }
            )
            return true
        }
        return true
    }
}
