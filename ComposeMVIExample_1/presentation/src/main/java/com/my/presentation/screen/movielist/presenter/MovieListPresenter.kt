package com.my.presentation.screen.movielist.presenter

import com.my.domain.model.MovieModelResult

sealed class MovieListPresenter {
    class MoveToMovieInfo(movieModelResult: MovieModelResult) : MovieListPresenter()
}