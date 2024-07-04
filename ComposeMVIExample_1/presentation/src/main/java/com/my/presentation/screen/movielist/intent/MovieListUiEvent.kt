package com.my.presentation.screen.movielist.intent

import com.my.domain.model.MovieModelResult

sealed interface MovieListUiEvent {
    class Success(val movieList: List<MovieModelResult>) : MovieListUiEvent
    class Fail(val code: String, val message: String?) : MovieListUiEvent
    class ExceptionHandle(val throwable: Throwable) : MovieListUiEvent
    class Loading(val isLoading: Boolean) : MovieListUiEvent
    class DataEmpty(val isDataEmpty: Boolean) : MovieListUiEvent
    class UpdateCurrentPosition(val currentPosition: Int) : MovieListUiEvent
}