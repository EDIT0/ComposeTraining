package com.my.presentation.screen.movielist.presenter

sealed class MovieListViewModelPresenter {
    class GetPopularMovies : MovieListViewModelPresenter()
    class UpdateCurrentPosition(val position: Int) : MovieListViewModelPresenter()
}