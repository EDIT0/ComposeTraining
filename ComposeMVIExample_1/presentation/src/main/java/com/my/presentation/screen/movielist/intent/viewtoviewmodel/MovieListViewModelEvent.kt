package com.my.presentation.screen.movielist.intent.viewtoviewmodel

sealed interface MovieListViewModelEvent {
    class GetPopularMovies(val page: Int, val size: Int) : MovieListViewModelEvent
    class UpdateCurrentPosition(val position: Int) : MovieListViewModelEvent
}