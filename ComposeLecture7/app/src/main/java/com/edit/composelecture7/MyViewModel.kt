package com.edit.composelecture7

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MyViewModel: ViewModel() {
    val numberFlow = flow<Int> {
        for(i in 0 until 100) {
            emit(i)
            delay(500L)
        }
    }

    init {
        backPressureDemo()
    }

    private fun backPressureDemo() {
        val numberFlow1 = flow<Int> {
            for(i in 0 until 10) {
                Log.i("MYTAG", "Produced ${i}")
                emit(i)
                delay(1000L)
            }
        }

        viewModelScope.launch {
            numberFlow1
                .filter {
                    it%3 == 0
                }
                .map {
                    showMessage(it)
                }.collectLatest {
//                    delay(2000L)
                    Log.i("MYTAG", "Consumed ${it}")
                }
        }
    }

    fun showMessage(count: Int) : String = "Hello ${count}"
}