package com.my.composexmldemo2

data class DataUiState(
    val data1: String = "",
    val data2: Int = 0
)

sealed interface DataUiErrorState {
    class Init : DataUiErrorState
    class Fail(val code: String, val message: String?) : DataUiErrorState
    class ExceptionHandle(val throwable: Throwable) : DataUiErrorState
    class DataEmpty(val isDataEmpty: Boolean) : DataUiErrorState
    class ConnectionError(val code: String, val message: String?) : DataUiErrorState
}