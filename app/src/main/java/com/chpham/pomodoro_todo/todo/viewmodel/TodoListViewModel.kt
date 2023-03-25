package com.chpham.pomodoro_todo.todo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chpham.domain.interactor.DeleteTaskUseCase
import com.chpham.domain.interactor.GetAllTasksUseCase
import com.chpham.domain.interactor.GetTaskByIdUseCase
import com.chpham.domain.interactor.InsertTaskUseCase
import com.chpham.domain.interactor.SetTaskStateUseCase
import com.chpham.domain.interactor.UpdateTaskUseCase
import com.chpham.domain.interactor.params.SetTaskStateParams
import com.chpham.domain.model.Task
import com.chpham.domain.model.TaskState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val setTaskStateUseCase: SetTaskStateUseCase,
    private val insertTaskUseCase: InsertTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    /**
     * LiveData that holds the list of tasks to be displayed on the UI.
     */
    private val _tasks = MutableLiveData<List<Task?>>()
    val tasks: LiveData<List<Task?>>
        get() = _tasks

    /**
     * Loads all tasks and updates the [_tasks] LiveData.
     */
    fun getAllTasks() {
        viewModelScope.launch {
            getAllTasksUseCase.execute().collect { tasks ->
                _tasks.value = tasks
            }
        }
    }

    /**
     * Loads a task with the specified [taskId] and returns a LiveData that holds the result.
     * @param taskId the ID of the task to load.
     * @return a LiveData of nullable [Task] object.
     */
    fun getTaskById(taskId: Int): LiveData<Task?> {
        val task = MutableLiveData<Task?>()
        viewModelScope.launch {
            getTaskByIdUseCase.execute(taskId).collect { taskValue ->
                task.value = taskValue
            }
        }
        return task
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
        viewModelScope.launch {
            insertTaskUseCase.execute(task)
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
}
