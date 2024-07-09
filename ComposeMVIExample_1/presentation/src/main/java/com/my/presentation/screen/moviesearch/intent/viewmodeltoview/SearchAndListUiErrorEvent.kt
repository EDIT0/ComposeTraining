package com.my.presentation.screen.moviesearch.intent.viewmodeltoview

sealed interface SearchAndListUiErrorEvent {
    class Init : SearchAndListUiErrorEvent
    class Fail(val code: String, val message: String?) : SearchAndListUiErrorEvent
    class ExceptionHandle(val throwable: Throwable) : SearchAndListUiErrorEvent
    class DataEmpty(val isDataEmpty: Boolean) : SearchAndListUiErrorEvent
}