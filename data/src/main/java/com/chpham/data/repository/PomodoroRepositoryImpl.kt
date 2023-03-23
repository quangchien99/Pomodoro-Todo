package com.chpham.data.repository

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Build
import android.os.Build.VERSION
import android.os.IBinder
import com.chpham.data.service.timer.CountDownTimerService
import com.chpham.data.utils.IntentKeys.COUNT_DOWN_TIMER_BROADCAST_ACTION
import com.chpham.data.utils.IntentKeys.COUNT_DOWN_TIMER_STATE_KEY
import com.chpham.data.utils.IntentKeys.LIMIT_COUNT_KEY
import com.chpham.domain.repository.PomodoroRepository
import com.chpham.domain.timer.CountDownTimerState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class PomodoroRepositoryImpl(private val context: Context) : PomodoroRepository {

    private var localBinder: CountDownTimerService.LocalBinder? = null

    override fun startCountDownTimer(limitCount: Int): Flow<CountDownTimerState> = callbackFlow {
        val countDownTimerServiceIntent = Intent(context, CountDownTimerService::class.java)
        countDownTimerServiceIntent.putExtra(LIMIT_COUNT_KEY, limitCount)

        val broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val state: CountDownTimerState? =
                    if (VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent?.getParcelableExtra(
                            COUNT_DOWN_TIMER_STATE_KEY,
                            CountDownTimerState::class.java
                        )
                    } else {
                        @Suppress("DEPRECATION")
                        intent?.getParcelableExtra(COUNT_DOWN_TIMER_STATE_KEY)
                    }
                state?.let {
                    when (it) {
                        CountDownTimerState.Started -> {
                            trySend(CountDownTimerState.Started).isSuccess
                        }
                        CountDownTimerState.Started -> {
                            trySend(CountDownTimerState.Started).isSuccess
                        }
                        // Case counting - return CountDownTimerState.Tick
                        else -> {
                            trySend(it).isSuccess
                        }
                    }
                }
            }
        }

        context.registerReceiver(broadcastReceiver, IntentFilter(COUNT_DOWN_TIMER_BROADCAST_ACTION))
        val serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                localBinder = service as CountDownTimerService.LocalBinder
                localBinder?.start(limitCount)
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                // No-op
            }
        }

        context.bindService(
            countDownTimerServiceIntent,
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )

        awaitClose {
            context.unregisterReceiver(broadcastReceiver)
            context.unbindService(serviceConnection)
        }
    }

    override fun pauseCountDownTimer() {
        localBinder?.pause()
    }

    override fun resumeCountDownTimer() {
        localBinder?.resume()
    }

    override fun restartCountDownTimer(limitCount: Int) {
        localBinder?.restartCountDownTimer(limitCount)
    }
}
