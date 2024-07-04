package com.my.presentation.screen.movielist.model

import com.my.domain.model.MovieModelResult

data class MovieListUiState(
    var movieList: List<MovieModelResult>? = null,
    val isLoading: Boolean = false,
    val code: String? = null,
    val message: String? = null,
    val throwable: Throwable? = null,
    val isDataEmpty: Boolean = false,
    val currentPosition: Int = -1
)