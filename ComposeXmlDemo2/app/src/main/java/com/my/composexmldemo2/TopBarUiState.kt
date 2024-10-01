package com.my.composexmldemo2

data class TopBarUiState(
    val loading: Boolean = false,
    val title: String = "",
)

sealed interface TopBarUiErrorState {
    class Init : TopBarUiErrorState
    class Fail(val code: String, val message: String?) : TopBarUiErrorState
    class ExceptionHandle(val throwable: Throwable) : TopBarUiErrorState
    class DataEmpty(val isDataEmpty: Boolean) : TopBarUiErrorState
    class ConnectionError(val code: String, val message: String?) : TopBarUiErrorState
}
