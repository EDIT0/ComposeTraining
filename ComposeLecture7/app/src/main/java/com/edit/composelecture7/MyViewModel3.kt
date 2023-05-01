package com.edit.composelecture7

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

// SharedFlow
// 값 변경 시 무조건 emit 해야함 (.value getter, setter 불가)
class MyViewModel3(
    startingTotal: Int
): ViewModel() {

    private val _message = MutableSharedFlow<String>()
    val message : SharedFlow<String> = _message

    private val _flowTotal = MutableStateFlow<Int>(0)
    val flowTotal : StateFlow<Int>
        get() {
            return _flowTotal
        }

    init {
        _flowTotal.value = startingTotal
    }

    suspend fun setTotal(input: Int) {
        // ? 마크를 붙히지 않아도 된다. 이유는 StateFlow는 항상 초기값을 가지고 있기 때문이다.
        // StateFlow는 null을 emit하지 않는다.
        _flowTotal.value = (_flowTotal.value).plus(input)
        _message.emit("Total Updated Successful")
    }

}