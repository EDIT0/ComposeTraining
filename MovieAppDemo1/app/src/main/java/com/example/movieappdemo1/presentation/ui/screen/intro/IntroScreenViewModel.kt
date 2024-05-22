package com.example.movieappdemo1.presentation.ui.screen.intro

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappdemo1.common.log.LogUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroScreenViewModel @Inject constructor(

) : ViewModel() {

    private val introSecondsTime = mutableIntStateOf(3)
    val timerFinish = mutableStateOf(false)
    val introData = "IntroData ${(1..100).random()}"

    init {
        startTimer()
    }

    fun setTimerFinish(value: Boolean) {
        timerFinish.value = value
    }

    private fun startTimer() {
        var currentSecondsTime = 0
        viewModelScope.launch {
            (1..introSecondsTime.intValue).asFlow().collect {
                delay(1000L)
                LogUtil.d_dev("현재 초: ${it}")
                currentSecondsTime = it
                if(currentSecondsTime == introSecondsTime.intValue) {
                    timerFinish.value = true
                }
            }
        }
    }
}