package com.my.presentation.screen.moviesearch.intent.viewtoview

import com.my.domain.model.MovieModelResult

sealed interface MovieSearchScreenEvent {
    class MoveToMovieInfo(movieModelResult: MovieModelResult) : MovieSearchScreenEvent
}