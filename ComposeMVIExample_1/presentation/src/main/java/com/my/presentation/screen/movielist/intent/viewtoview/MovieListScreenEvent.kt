package com.my.presentation.screen.movielist.intent.viewtoview

import com.my.domain.model.MovieModelResult

sealed interface MovieListScreenEvent {
    class MoveToMovieInfo(movieModelResult: MovieModelResult) : MovieListScreenEvent
    class MoveToMovieSearch() : MovieListScreenEvent
}