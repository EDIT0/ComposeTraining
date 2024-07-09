package com.my.presentation.screen.moviesearch.intent.viewtoviewmodel

sealed interface MovieSearchViewModelEvent {
    class GetSearchMovies(val query: String, val isClear: Boolean) : MovieSearchViewModelEvent
    class UpdateCurrentPosition(val position: Int) : MovieSearchViewModelEvent
    class UpdateSearchMovieSearchText(val text: String) : MovieSearchViewModelEvent
}