package com.chpham.domain.repository

import com.chpham.domain.timer.CountDownTimerState
import kotlinx.coroutines.flow.Flow

interface PomodoroRepository {

    fun startCountDownTimer(limitCount: Int): Flow<CountDownTimerState>

    fun pauseCountDownTimer()

    fun resumeCountDownTimer()

    fun restartCountDownTimer(limitCount: Int)
}