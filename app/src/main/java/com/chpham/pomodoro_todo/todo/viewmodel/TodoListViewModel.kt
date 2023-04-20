package com.chpham.pomodoro_todo.todo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chpham.data.local.preferences.SharedPreferencesDataSource
import com.chpham.domain.alarm.AlarmScheduler
import com.chpham.domain.interactor.DeleteTaskUseCase
import com.chpham.domain.interactor.GetAllTasksUseCase
import com.chpham.domain.interactor.GetTaskByIdUseCase
import com.chpham.domain.interactor.GetTasksByDayUseCase
import com.chpham.domain.interactor.GetTasksInRangeUseCase
import com.chpham.domain.interactor.InsertTaskUseCase
import com.chpham.domain.interactor.SetTaskStateUseCase
import com.chpham.domain.interactor.UpdateTaskUseCase
import com.chpham.domain.interactor.params.GetTaskInRangeParams
import com.chpham.domain.interactor.params.SetTaskStateParams
import com.chpham.domain.model.AlarmItem
import com.chpham.domain.model.RemindOptions
import com.chpham.domain.model.Task
import com.chpham.domain.model.TaskState
import com.chpham.pomodoro_todo.base.viewmodel.ViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

/**
 * ViewModel for the to do list screen.
 * @property getAllTasksUseCase instance of [GetAllTasksUseCase] used to get all tasks.
 * @property getTaskByIdUseCase instance of [GetTaskByIdUseCase] used to get a task by ID.
 * @property setTaskStateUseCase instance of [SetTaskStateUseCase] used to update a task's state.
 * @property insertTaskUseCase instance of [InsertTaskUseCase] used to insert a new task.
 * @property updateTaskUseCase instance of [UpdateTaskUseCase] used to update an existing task.
 * @property deleteTaskUseCase instance of [DeleteTaskUseCase] used to delete a task.
 *
 * @since March 19, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */
@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val getTasksByDayUseCase: GetTasksByDayUseCase,
    private val getTasksInRangeUseCase: GetTasksInRangeUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val setTaskStateUseCase: SetTaskStateUseCase,
    private val insertTaskUseCase: InsertTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val sharedPreferencesDataSource: SharedPreferencesDataSource,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    private val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    private val todayDate = calendar.timeInMillis
    private val yesterdayDate = calendar.apply { add(Calendar.DAY_OF_YEAR, -1) }.timeInMillis
    private val next1DayDate = calendar.apply { add(Calendar.DAY_OF_YEAR, 2) }.timeInMillis
    private val next7DaysDate = calendar.apply { add(Calendar.DAY_OF_YEAR, 6) }.timeInMillis

    private val _state = MutableLiveData<ViewModelState>()
    val state: LiveData<ViewModelState>
        get() = _state

    /**
     * LiveData that holds the list of all tasks.
     */
    private val _allTasks = MutableLiveData<List<Task?>>()
    val allTasks: LiveData<List<Task?>>
        get() = _allTasks

    /**
     * LiveData that holds the list of all yesterday tasks.
     */
    private val _yesterdayTasks = MutableLiveData<List<Task?>>()
    val yesterdayTasks: LiveData<List<Task?>>
        get() = _yesterdayTasks

    /**
     * LiveData that holds the list of all yesterday tasks.
     */
    private val _todayTasks = MutableLiveData<List<Task?>>()
    val todayTasks: LiveData<List<Task?>>
        get() = _todayTasks

    private val _next7DaysTasks = MutableLiveData<List<Task?>>()
    val next7DaysTasks: LiveData<List<Task?>>
        get() = _next7DaysTasks

    private val _categories = MutableLiveData<List<String>>()
    val categories: LiveData<List<String>>
        get() = _categories

    init {
        _state.value = ViewModelState.LOADING
    }

    private fun setState(state: ViewModelState) {
        _state.value = state
    }

    /**
     * Loads all tasks and updates the [_allTasks] LiveData.
     */
    fun getAllTasks() {
        viewModelScope.launch {
            getAllTasksUseCase.execute().collect { tasks ->
                _allTasks.value = tasks
            }
        }
    }

    fun getYesterdayTasks() {
        viewModelScope.launch {
            getTasksByDayUseCase.execute(
                yesterdayDate
            ).collect { tasks ->
                _yesterdayTasks.value = tasks
            }
        }
    }

    fun getTodayTasks() {
        viewModelScope.launch {
            getTasksByDayUseCase.execute(
                todayDate

            ).collect { tasks ->
                _todayTasks.value = tasks
            }
        }
    }

    fun getNext7DaysTasks() {
        viewModelScope.launch {
            getTasksInRangeUseCase.execute(
                GetTaskInRangeParams(next1DayDate, next7DaysDate)
            ).collect { tasks ->
                _next7DaysTasks.value = tasks
            }
        }
    }

    /**
     * Loads a task with the specified [taskId] and returns a LiveData that holds the result.
     * @param taskId the ID of the task to load.
     * @return a LiveData of nullable [Task] object.
     */
    fun getTaskById(taskId: Int, callback: (Task?) -> Unit) {
        viewModelScope.launch {
            val task = getTaskByIdUseCase.execute(taskId).firstOrNull()
            callback(task)
        }
    }

    /**
     * Sets the state of a task with the specified [taskId] and [taskState].
     * @param taskId the ID of the task to update.
     * @param taskState the new state of the task.
     */
    fun setTaskState(taskId: Int, taskState: TaskState) {
        viewModelScope.launch {
            setTaskStateUseCase.execute(SetTaskStateParams(taskId, taskState))
        }
    }

    /**
     * Inserts a new task with the specified [task].
     * @param task the new task.
     */
    fun insertTask(task: Task) {
        setState(ViewModelState.INSERTING)
        viewModelScope.launch {
            viewModelScope.launch {
                val result = insertTaskUseCase.execute(task)
                if (result != null) {
                    setState(ViewModelState.InsertSucceeded(result.toInt()))
                } else {
                    setState(ViewModelState.ERROR)
                }
            }
        }
    }

    /**
     * Updates an existing task with the specified updated [task].
     * @param task the task to update.
     */
    fun updateTask(task: Task) {
        viewModelScope.launch {
            updateTaskUseCase.execute(task)
        }
    }

    /**
     * Deletes a task with the specified [taskId].
     * @param taskId the ID of the task to delete.
     */
    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            deleteTaskUseCase.execute(taskId)
        }
    }

    fun insertCategory(category: String) {
        sharedPreferencesDataSource.insertCategory(category)
        _categories.value = sharedPreferencesDataSource.getCategories()
    }

    fun getCategories() {
        _categories.value = sharedPreferencesDataSource.getCategories()
    }

    fun createAlarm(
        id: Int,
        remindTime: Long,
        message: String,
        startDate: Long,
        remindOptions: RemindOptions?
    ) {
        alarmScheduler.schedule(
            AlarmItem(
                id = id,
                time = remindTime,
                message = message,
                startDate = startDate,
                remindOptions = remindOptions
            )
        )
    }
}
