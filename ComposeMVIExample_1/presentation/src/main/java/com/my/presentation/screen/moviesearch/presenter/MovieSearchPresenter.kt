package com.my.presentation.screen.moviesearch.presenter

import com.my.domain.model.MovieModelResult

sealed class MovieSearchPresenter {
    class MoveToMovieInfo(movieModelResult: MovieModelResult) : MovieSearchPresenter()
}