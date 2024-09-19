package com.my.presentation.screen.movielist.state

import com.my.domain.model.MovieModel

data class MovieListUiState(
    var movieModel: MovieModel? = null,
    val isLoading: Boolean = false,
//    val code: String? = null,
//    val message: String? = null,
//    val throwable: Throwable? = null,
//    val isDataEmpty: Boolean = false,
    val currentPosition: Int = -1
)