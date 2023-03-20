package com.chpham.data.service.timer

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.chpham.data.broadcast.timer.CountDownTimerBroadcastFactory
import com.chpham.data.utils.IntentKeys.LIMIT_COUNT_KEY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

class CountDownTimerService : Service() {

    private val parentScope = CoroutineScope(Job() + Dispatchers.Default)
    private lateinit var countDownTimer: CountDownTimer
    private var isPaused = false

    private val countDownTimerListener = object : CountDownTimer.CountDownTimerListener {
        override fun onStart() {
            sendBroadcast(CountDownTimerBroadcastFactory.createStartedIntent())
        }

        override fun onTick(currentCount: Int) {
            if (!isPaused) {
                sendBroadcast(CountDownTimerBroadcastFactory.createTickIntent(currentCount))
            }
        }

        override fun onTimeLimit() {
            sendBroadcast(CountDownTimerBroadcastFactory.createFinishedIntent())
            stopSelf()
        }
    }

    override fun onCreate() {
        super.onCreate()
        countDownTimer = CountDownTimer(parentScope)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val limitCount = intent?.getIntExtra(LIMIT_COUNT_KEY, 0) ?: 0
        countDownTimer.start(limitCount, countDownTimerListener)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.stop()
        parentScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder {
        return LocalBinder()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    inner class LocalBinder : Binder() {

        fun start(limitCount: Int) {
            return countDownTimer.start(limitCount, countDownTimerListener)
        }

        fun pause() {
            isPaused = true
            countDownTimer.pause()
        }

        fun resume() {
            isPaused = false
            countDownTimer.resume()
        }

        fun restartCountDownTimer(limitCount: Int) {
            isPaused = true
            countDownTimer.restart(limitCount)
        }
    }
}