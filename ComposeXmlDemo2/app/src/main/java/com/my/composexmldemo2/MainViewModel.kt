package com.my.composexmldemo2

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    val text1 = mutableStateOf("Text1")
    val count = MutableStateFlow<Int>(0)

    private val _dataUiState = MutableStateFlow<DataUiState>(DataUiState())
    val dataUiState: StateFlow<DataUiState> = _dataUiState

    private val _dataUiErrorState = MutableStateFlow<DataUiErrorState>(DataUiErrorState.Init())
    val dataUiErrorState: StateFlow<DataUiErrorState> = _dataUiErrorState

    private val _topBarUiState = MutableStateFlow<TopBarUiState>(TopBarUiState())
    val topBarUiState: StateFlow<TopBarUiState> = _topBarUiState

    private val _topBarUiErrorState = MutableStateFlow<TopBarUiErrorState>(TopBarUiErrorState.Init())
    val topBarUiErrorState: StateFlow<TopBarUiErrorState> = _topBarUiErrorState

    init {
        viewModelScope.launch {
            for(i in 0 until 10) {
                delay(2000L)
                val c = count.value + 1
                count.emit(c)
                text1.value = "Text1 ${count.value}"

                _topBarUiState.emit(topBarUiState.value.copy(loading = false))
                _dataUiState.emit(dataUiState.value.copy(data1 = "${i}번", data2 = dataUiState.value.data2 + i))
                _topBarUiState.emit(topBarUiState.value.copy(title = "타이틀 ${i}"))
                _dataUiState.emit(dataUiState.value.copy(data1 = "${i+100}번", data2 = dataUiState.value.data2 + i + 100))
                _topBarUiErrorState.emit(TopBarUiErrorState.Fail(code = "TopBarUi Fail", message = "TopBarUi Fail"))
                _topBarUiState.emit(topBarUiState.value.copy(loading = true))
                _dataUiErrorState.emit(DataUiErrorState.Fail(code = "DataUi Fail", message = "DataUi Fail"))
            }
        }
    }
}