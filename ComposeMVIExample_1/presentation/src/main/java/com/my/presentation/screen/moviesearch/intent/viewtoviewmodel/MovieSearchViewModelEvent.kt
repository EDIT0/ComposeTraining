package com.my.presentation.screen.moviesearch.intent.viewtoviewmodel

sealed interface MovieSearchViewModelEvent {
    class GetPopularMovies : MovieSearchViewModelEvent
    class UpdateCurrentPosition(val position: Int) : MovieSearchViewModelEvent
}