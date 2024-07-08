package com.my.presentation.screen.movielist.intent.viewmodeltoview

sealed interface MovieListUiErrorEvent {
    class Init : MovieListUiErrorEvent
    class Fail(val code: String, val message: String?) : MovieListUiErrorEvent
    class ExceptionHandle(val throwable: Throwable) : MovieListUiErrorEvent
    class DataEmpty(val isDataEmpty: Boolean) : MovieListUiErrorEvent
}