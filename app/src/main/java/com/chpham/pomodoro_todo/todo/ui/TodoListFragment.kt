package com.chpham.pomodoro_todo.todo.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
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
import com.chpham.pomodoro_todo.todo.ui.dialog.CreateTaskBottomSheetDialogFragment
import com.chpham.pomodoro_todo.todo.viewmodel.TodoListViewModel
import com.chpham.pomodoro_todo.utils.Constants.HEADER_DONE
import com.chpham.pomodoro_todo.utils.Constants.HEADER_IN_PROGRESS
import com.chpham.pomodoro_todo.utils.Constants.HEADER_TODO
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodoListFragment : BaseFragment<FragmentTodoBinding>() {

    companion object {
        fun newInstance(): TodoListFragment {
            return TodoListFragment()
        }
    }

    private lateinit var previousTasksAdapter: TasksAdapter
    private lateinit var todayTasksAdapter: TasksAndHeadersAdapter
    private lateinit var next7DaysTasksAdapter: TasksAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    private var bottomSheetDialogFragment: CreateTaskBottomSheetDialogFragment? = null

    private val todoListViewModel: TodoListViewModel by activityViewModels()

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
            bottomSheetDialogFragment = CreateTaskBottomSheetDialogFragment()
            activity?.supportFragmentManager?.let {
                bottomSheetDialogFragment?.show(it, CreateTaskBottomSheetDialogFragment.TAG)
            }
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
                Log.e("ChienNgan", "category Clicked")
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
            override fun onTaskClick(taskId: Int, card: CardView) {
                Log.e("ChienNgan", "todayTaskClickListener: onTaskClick")
                TODO("Not yet implemented")
            }

            override fun onTaskDoneClick(task: Task) {
                Log.e("ChienNgan", "todayTaskClickListener: onTaskDoneClick")
                TODO("Not yet implemented")
            }

            override fun onTaskDoingClick(task: Task) {
                Log.e("ChienNgan", "todayTaskClickListener: onTaskDoingClick")
                TODO("Not yet implemented")
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
        val categories = mutableListOf<String>()

        todoListViewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                ViewModelState.INSERTING -> {
                    bottomSheetDialogFragment?.dismiss()
                    bottomSheetDialogFragment?.onDestroy()
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
        todoListViewModel.todayTasks.observe(viewLifecycleOwner) { tasks ->
            if (tasks.isNullOrEmpty()) {
                binding.tvEmptyTasks.visibility = View.VISIBLE
                binding.animEmptyTasks.visibility = View.VISIBLE
            } else {
                binding.tvEmptyTasks.visibility = View.GONE
                binding.animEmptyTasks.visibility = View.GONE

                val todoTasks = tasks.filter { it?.state == TaskState.TO_DO }
                val inProgressTasks = tasks.filter { it?.state == TaskState.IN_PROGRESS }
                val doneTasks = tasks.filter { it?.state == TaskState.DONE }

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
        todoListViewModel.next7DaysTasks.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.tvFuture.visibility = View.GONE
            } else {
                binding.tvFuture.visibility = View.VISIBLE
            }
            next7DaysTasksAdapter.differ.submitList(it)
        }

        categories.add("All")
        categories.add("Study")
        categories.add("Family")
        categories.add("Work")
        categories.add("School")
        categories.add("Love")
        categories.add("Travel")
        categoriesAdapter.differ.submitList(categories)
    }

    private fun initClickListener() {
        setHeaderClickListener(
            binding.layoutHeaderPrevious,
            binding.rcvPreviousTasks,
            binding.tvPrevious
        )

        setHeaderClickListener(
            binding.layoutHeaderToday,
            binding.rcvTodayTasks,
            binding.tvToday
        )

        setHeaderClickListener(
            binding.layoutHeaderFuture,
            binding.rcvNext7DaysTasks,
            binding.tvFuture
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
}
