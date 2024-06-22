package com.example.movieappdemo1.presentation.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object SearchDelayUtil {
    private var isPass = true
    private var timer = 0L
    private var isTimerOn = false

    fun onDelay(
        lastStopCallback: () -> Unit
    ): Boolean {
        if(isPass) {
            timer = 0L
            CoroutineScope(Dispatchers.Main).launch {
                isPass = false
                delay(1000L)
                isPass = true
            }
            return true
        } else {
            if(isTimerOn) {

            } else {
                startTimer(lastStopCallback).cancel()
                startTimer(lastStopCallback).start()
            }
            return false
        }
    }

    private fun startTimer(lastStopCallback: () -> Unit): Job {
        isTimerOn = true
        return CoroutineScope(Dispatchers.Main).launch {
            while (isTimerOn) {
                timer += 10L
                delay(10L)
                if(timer > 1010L) {
                    lastStopCallback.invoke()
                    isTimerOn = false
                    cancel()
                    break
                }
            }
        }
    }
}