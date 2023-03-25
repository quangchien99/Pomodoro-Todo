package com.chpham.pomodoro_todo.pomodoro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chpham.domain.interactor.CountDownTimerUseCase
import com.chpham.domain.timer.CountDownTimerState
import com.chpham.pomodoro_todo.base.viewmodel.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * ViewModel class for the Pomodoro timer feature.
 *
 * @property countDownTimerUseCase Use case for the count down timer.
 * @property counterJob Job that runs the count down timer.
 * @property _countDownTimerState MutableLiveData that contains the current state of the count down timer.
 * @property countDownTimerState LiveData that exposes the current state of the count down timer.
 *
 *
 * @since March 25, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */
class PomodoroViewModel(
    private val countDownTimerUseCase: CountDownTimerUseCase
) : BaseViewModel<Any>() {

    private var counterJob: Job? = null

    private val _countDownTimerState = MutableLiveData<CountDownTimerState>()
    val countDownTimerState: LiveData<CountDownTimerState> = _countDownTimerState

    /**
     * Starts the count down timer with the given [limitCount].
     *
     * Cancels any previous counter job if one exists, and launches a new job for the count down timer.
     * The job collects the [Flow] of [CountDownTimerState] returned by the [countDownTimerUseCase],
     * and updates the [_countDownTimerState] accordingly.
     *
     * @param limitCount The initial limit count for the count down timer.
     */
    fun startCountDownTimer(limitCount: Int) {
        counterJob?.cancel()
        counterJob = viewModelScope.launch(handlerException) {
            countDownTimerUseCase.execute(limitCount).collect {
                _countDownTimerState.value = it
            }
        }
    }

    /**
     * Pauses the count down timer.
     */
    fun pauseCountDownTimer() {
        countDownTimerUseCase.pauseCountDownTimer()
    }

    /**
     * Resumes the count down timer.
     */
    fun resumeCountDownTimer() {
        countDownTimerUseCase.resumeCountDownTimer()
    }

    /**
     * Restarts the count down timer with the given [limitCount].
     *
     * @param limitCount The new limit count for the count down timer.
     */
    fun restartCountDownTimer(limitCount: Int) {
        countDownTimerUseCase.restartCountDownTimer(limitCount)
    }

    override fun initState() {
        // do nothing
    }
}
