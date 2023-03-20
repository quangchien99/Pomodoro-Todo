package com.chpham.pomodoro_todo.pomodoro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chpham.domain.interactor.CountDownTimerUseCase
import com.chpham.domain.timer.CountDownTimerState
import com.chpham.pomodoro_todo.base.viewmodel.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PomodoroViewModel(
    private val countDownTimerUseCase: CountDownTimerUseCase
) : BaseViewModel<Any>() {

    private var counterJob: Job? = null

    private val _countDownTimerState = MutableLiveData<CountDownTimerState>()
    val countDownTimerState: LiveData<CountDownTimerState> = _countDownTimerState


    fun startCountDownTimer(limitCount: Int) {
        counterJob?.cancel()
        counterJob = viewModelScope.launch(handlerException) {
            countDownTimerUseCase.execute(limitCount).collect {
                _countDownTimerState.value = it
            }
        }
    }

    fun pauseCountDownTimer() {
        countDownTimerUseCase.pauseCountDownTimer()
    }

    fun resumeCountDownTimer() {
        countDownTimerUseCase.resumeCountDownTimer()
    }

    fun restartCountDownTimer(limitCount: Int) {
        countDownTimerUseCase.restartCountDownTimer(limitCount)
    }

    override fun initState() {
        //do nothing
    }


}