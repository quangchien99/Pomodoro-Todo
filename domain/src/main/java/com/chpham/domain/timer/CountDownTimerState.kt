package com.chpham.domain.timer

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class CountDownTimerState : Parcelable {
    object NotStarted : CountDownTimerState()
    object Started : CountDownTimerState()
    object Finished : CountDownTimerState()
    object Counting : CountDownTimerState()
    data class Tick(val currentCount: Int) : CountDownTimerState()
    object Paused : CountDownTimerState()
}
