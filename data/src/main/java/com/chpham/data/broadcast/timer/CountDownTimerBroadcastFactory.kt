package com.chpham.data.broadcast.timer

import android.content.Intent
import com.chpham.data.utils.IntentKeys.COUNT_DOWN_TIMER_BROADCAST_ACTION
import com.chpham.data.utils.IntentKeys.COUNT_DOWN_TIMER_STATE_KEY
import com.chpham.domain.timer.CountDownTimerState

object CountDownTimerBroadcastFactory {

    fun createStartedIntent(): Intent {
        return Intent(COUNT_DOWN_TIMER_BROADCAST_ACTION).apply {
            putExtra(COUNT_DOWN_TIMER_STATE_KEY, CountDownTimerState.Started)
        }
    }

    fun createTickIntent(currentCount: Int): Intent {
        return Intent(COUNT_DOWN_TIMER_BROADCAST_ACTION).apply {
            putExtra(COUNT_DOWN_TIMER_STATE_KEY, CountDownTimerState.Tick(currentCount))
        }
    }

    fun createFinishedIntent(): Intent {
        return Intent(COUNT_DOWN_TIMER_BROADCAST_ACTION).apply {
            putExtra(COUNT_DOWN_TIMER_STATE_KEY, CountDownTimerState.Finished)
        }
    }
}