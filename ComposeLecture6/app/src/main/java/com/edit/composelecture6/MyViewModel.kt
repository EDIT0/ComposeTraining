package com.edit.composelecture6

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {
    var count by mutableStateOf<Int>(0)

    fun increaseCount() {
        count++
    }
}