package com.chpham.data.service.timer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield

/**
 * A simple countdown timer implementation that runs on a coroutine.
 *
 * @property parentScope The [CoroutineScope] instance that will be used to launch the coroutine.
 *
 * @since March 19, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */
class CountDownTimer(private val parentScope: CoroutineScope) {

    /**
     * The [Job] instance that represents the running coroutine.
     */

    private var job: Job? = null

    /**
     * A flag indicating whether the countdown timer is currently paused.
     */
    private var isPaused: Boolean = false

    /**
     * A flag indicating whether decrease the time immediately or not .
     */
    private var shouldSkipDelay: Boolean = true

    /**
     * The remaining time on the countdown timer.
     */
    private var timeRemaining: Int = 0

    /**
     * An interface for receiving callbacks during the countdown timer lifecycle.
     */
    interface CountDownTimerListener {

        /**
         * Called when the countdown timer is started.
         */
        fun onStart()

        /**
         * Called on each tick of the countdown timer.
         *
         * @param currentCount The current count of the countdown timer.
         */
        fun onTick(currentCount: Int)

        /**
         * Called when the countdown timer reaches its time limit.
         */
        fun onTimeLimit()
    }

    /**
     * The listener that will receive callbacks during the countdown timer lifecycle.
     */
    private var listener: CountDownTimerListener? = null

    /**
     * The [Dispatchers.Main] dispatcher instance for updating the UI.
     */
    private val mainDispatcher = Dispatchers.Main

    /**
     * Starts the countdown timer with the given time limit and listener.
     *
     * @param limitCount The time limit of the countdown timer.
     * @param countDownTimerListener The listener to receive callbacks during the countdown timer lifecycle.
     */
    fun start(limitCount: Int, countDownTimerListener: CountDownTimerListener?) {
        listener = countDownTimerListener
        timeRemaining = limitCount

        job = parentScope.launch {
            withContext(Dispatchers.Main) { listener?.onStart() }

            while (timeRemaining > 0) {
                /**
                 * The yield() function is used in the else block of the if (!isPaused) statement
                 * to give the opportunity for other coroutines to execute.
                 * The delay() function is used to pause the coroutine execution for a specific amount of time, which in this case is 1 second.
                 * It is used to achieve the countdown functionality by reducing the time remaining by 1 second every time the loop runs.
                 * The combination of delay() and yield() ensures that the coroutine does not block the thread while waiting for the next tick to occur, and allows other coroutines to execute while the countdown is paused.
                 */
                if (!isPaused) {
                    if (!shouldSkipDelay) {
                        delay(1000)
                    }
                    shouldSkipDelay = false
                    timeRemaining--
                    withContext(mainDispatcher) {
                        listener?.onTick(timeRemaining)
                    }
                } else {
                    yield()
                }
            }
            withContext(Dispatchers.Main) {
                listener?.onTimeLimit()
                stop()
            }
        }
    }

    /**
     * Pauses the countdown timer.
     */
    fun pause() {
        isPaused = true
    }

    /**
     * Resumes the countdown timer.
     */
    fun resume() {
        isPaused = false
        shouldSkipDelay = true
        timeRemaining++
    }

    /**
     * Set the time to restart.
     */
    fun restart(time: Int) {
        isPaused = true
        shouldSkipDelay = true
        timeRemaining = time
    }

    /**
     * Stops the countdown timer.
     */
    fun stop() {
        job?.cancel()
        listener = null
    }
}
