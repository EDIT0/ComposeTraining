package com.my.presentation.screen.test.intent

sealed interface StringListUiEvent {
    class Success(val data: List<String>) : StringListUiEvent
    class Fail(val code: String, val message: String?) : StringListUiEvent
    class ExceptionHandle(val throwable: Throwable) : StringListUiEvent
    class Loading(val isLoading: Boolean) : StringListUiEvent
    class DataEmpty(val isDataEmpty: Boolean) : StringListUiEvent
}