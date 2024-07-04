package com.my.presentation.screen.test.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.domain.model.base.RequestResult
import com.my.domain.usecase.test.GetStringListUseCase
import com.my.presentation.screen.test.intent.StringListUiEvent
import com.my.presentation.screen.test.model.StringListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Intent(Event): 컨테이너 내에 있는 상태 및 부수효과를 변경하기 위한 빌드 함수
 * Reduce: 현재의 상태와 전달 받은 이벤트를 참고하여 새로운 상태를 만드는 것
 * SideEffect:  상태 변경과 관련 없는 이벤트들을 처리하기 위한 부수효과를 발생
 * */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val getStringListUseCase: GetStringListUseCase
) : ViewModel() {

    private val stringListUiEventChannel = Channel<StringListUiEvent>()

    val stringListUiState: StateFlow<StringListUiState> = stringListUiEventChannel.receiveAsFlow()
        .runningFold(StringListUiState(), ::reduceStringListUiState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, StringListUiState())

    private val _sideEffects = Channel<String>()

    val sideEffects = _sideEffects.receiveAsFlow()

    private fun reduceStringListUiState(stringListUiState: StringListUiState, stringListUiEvent: StringListUiEvent) : StringListUiState {
        return when(stringListUiEvent){
            is StringListUiEvent.Success -> {
                stringListUiState.copy(stringList = stringListUiEvent.data, code = null, message = null, throwable = null, isDataEmpty = false)
            }
            is StringListUiEvent.Fail -> {
                stringListUiState.copy(code = stringListUiEvent.code, message = stringListUiEvent.message, throwable = null, isDataEmpty = false)
            }
            is StringListUiEvent.ExceptionHandle -> {
                stringListUiState.copy(code = null, message = null, throwable = stringListUiEvent.throwable, isDataEmpty = false)
            }
            is StringListUiEvent.Loading -> {
                stringListUiState.copy(isLoading = stringListUiEvent.isLoading, isDataEmpty = false)
            }
            is StringListUiEvent.DataEmpty -> {
                stringListUiState.copy(isDataEmpty = true)
            }
        }
    }

    fun getStringList(data1: String, key: String) {
        viewModelScope.launch {
            stringListUiEventChannel.send(StringListUiEvent.Loading(isLoading = true))
            val responseResult = getStringListUseCase.invoke(data1, key)
            responseResult
                .onStart {  }
                .onCompletion {  }
                .filter {
                    val errorCode = "${it.code}${Math.random()}"?:""
                    val errorMessage = it.message

                    if(it is RequestResult.Error) {
                        stringListUiEventChannel.send(StringListUiEvent.Fail(errorCode, errorMessage))
                    } else if(it is RequestResult.ConnectionError) {
                        stringListUiEventChannel.send(StringListUiEvent.Fail(errorCode, errorMessage))
                    }

                    if(it is RequestResult.DataEmpty) {
                        stringListUiEventChannel.send(StringListUiEvent.DataEmpty(isDataEmpty = true))
                    }

                    return@filter it is RequestResult.Success
                }
                .catch {
                    stringListUiEventChannel.send(StringListUiEvent.ExceptionHandle(throwable = it))
                }
                .map {
                    it.resultData
                }
                .collect { resultData ->
                    resultData?.let {
                        if(stringListUiState.value.stringList == null || stringListUiState.value.stringList!!.isEmpty()) {
                            stringListUiEventChannel.send(StringListUiEvent.Success(data = it))
                        } else {
                            val arrList: ArrayList<String> = ArrayList(stringListUiState.value.stringList!!)
                            arrList.addAll(it)
                            stringListUiState.value.stringList = arrList
                            stringListUiEventChannel.send(StringListUiEvent.Success(data = stringListUiState.value.stringList!!))
                        }
                    }
                }
            stringListUiEventChannel.send(StringListUiEvent.Loading(isLoading = false))
        }
    }

}