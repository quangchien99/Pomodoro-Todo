package com.chpham.pomodoro_todo.base.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chpham.data.repository.PomodoroRepositoryImpl
import com.chpham.domain.interactor.CountDownTimerUseCase
import com.chpham.pomodoro_todo.pomodoro.viewmodel.PomodoroViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PomodoroViewModel::class.java)) {
            val repository = PomodoroRepositoryImpl(context)
            val countDownTimerUseCase = CountDownTimerUseCase(repository)
            @Suppress("UNCHECKED_CAST")
            return PomodoroViewModel(countDownTimerUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}