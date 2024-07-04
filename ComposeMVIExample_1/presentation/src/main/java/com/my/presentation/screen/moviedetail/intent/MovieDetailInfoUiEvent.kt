package com.my.presentation.screen.moviedetail.intent

import com.my.domain.model.MovieModelResult

sealed interface MovieDetailInfoUiEvent {
    class UpdateMovieDetailInfo(val movieModelResult: MovieModelResult) : MovieDetailInfoUiEvent
}