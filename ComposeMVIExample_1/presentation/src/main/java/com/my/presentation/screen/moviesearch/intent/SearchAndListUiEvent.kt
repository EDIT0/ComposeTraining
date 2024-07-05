package com.my.presentation.screen.moviesearch.intent

import com.my.domain.model.MovieModelResult

sealed interface SearchAndListUiEvent {
    class Success(val searchedMovieList: List<MovieModelResult>) : SearchAndListUiEvent
    class Fail(val code: String, val message: String?) : SearchAndListUiEvent
    class ExceptionHandle(val throwable: Throwable) : SearchAndListUiEvent
    class Loading(val isLoading: Boolean) : SearchAndListUiEvent
    class DataEmpty(val isDataEmpty: Boolean) : SearchAndListUiEvent
    class UpdateSearchText(val searchText: String) : SearchAndListUiEvent
    class UpdateCurrentPosition(val currentPosition: Int) : SearchAndListUiEvent
}