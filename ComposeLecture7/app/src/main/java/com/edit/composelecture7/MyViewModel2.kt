package com.edit.composelecture7

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// StateFlow
// 1. StateFlow는 반드시 초기값 명시
// 2. StateFlow는 value 속성을 이용해 값을 방출(또는 가져올수도 있죠)
// 3. SharedFlow는 좀 더 detail한 설정값들을 생성자에 전달 가능
// 그리고 가장 큰 차이점은 StateFlow는 값을 중복으로 방출할경우, collect()해오지 않는다.
class MyViewModel2(
    startingTotal: Int
) : ViewModel() {

    /**
     * Cold Stream은 컨슈머가 collect 시작하기 전까지는 값 생산을 시작하지 않는다.
     * Hot Stream은 즉시 값을 생산한다.
     * */
    private val _flowTotal = MutableStateFlow<Int>(0)
    val flowTotal : StateFlow<Int>
        get() {
            return _flowTotal
        }
    /**
     * LiveData와 StateFlow의 차이점은 초기값이 있는지 없는지이다.
     * LiveData는 자동적으로 view가 stop될 때 자동적으로 등록을 해제하지만, StateFlow는 그렇지 못한다.
     * 그래서 lifecycleScope를 사용할 필요가 있다.
     * LiveData는 안드로이드 프레임워크이고, StateFlow는 코틀린 언어의 일부이다. 코틀린 멀티플랫 프로젝트를 만들 때 유용할 수 있다.
     * */

    init {
        _flowTotal.value = startingTotal
    }

    fun setTotal(input: Int) {
        // ? 마크를 붙히지 않아도 된다. 이유는 StateFlow는 항상 초기값을 가지고 있기 때문이다.
        // StateFlow는 null을 emit하지 않는다.
        _flowTotal.value = (_flowTotal.value).plus(input)
    }
}