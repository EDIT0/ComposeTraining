package com.my.presentation.screen.moviesearch.presenter

sealed class MovieSearchViewModelPresenter {
    class GetPopularMovies : MovieSearchViewModelPresenter()
    class UpdateCurrentPosition(val position: Int) : MovieSearchViewModelPresenter()
}