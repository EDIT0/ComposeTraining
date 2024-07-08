package com.my.presentation.screen.movielist.intent.viewtoviewmodel

sealed interface MovieListViewModelEvent {
    class GetPopularMovies : MovieListViewModelEvent
    class UpdateCurrentPosition(val position: Int) : MovieListViewModelEvent
}