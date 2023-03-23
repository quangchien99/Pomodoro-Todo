package com.chpham.domain.interactor

import com.chpham.domain.SingleUseCaseWithParameter
import com.chpham.domain.repository.PomodoroRepository
import com.chpham.domain.timer.CountDownTimerState
import kotlinx.coroutines.flow.Flow

class CountDownTimerUseCase(private val pomodoroRepository: PomodoroRepository) :
    SingleUseCaseWithParameter<Int, Flow<CountDownTimerState>> {

    override suspend fun execute(parameter: Int): Flow<CountDownTimerState> {
        return pomodoroRepository.startCountDownTimer(parameter)
    }

    fun pauseCountDownTimer() {
        pomodoroRepository.pauseCountDownTimer()
    }

    fun resumeCountDownTimer() {
        pomodoroRepository.resumeCountDownTimer()
    }

    fun restartCountDownTimer(limitCount: Int) {
        pomodoroRepository.restartCountDownTimer(limitCount)
    }
}
