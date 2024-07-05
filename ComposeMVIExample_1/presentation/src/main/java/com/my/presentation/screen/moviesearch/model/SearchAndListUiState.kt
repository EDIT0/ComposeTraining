package com.my.presentation.screen.moviesearch.model

import com.my.domain.model.MovieModelResult

data class SearchAndListUiState(
    var searchedMovieList: List<MovieModelResult>? = null,
    val isLoading: Boolean = false,
    val code: String? = null,
    val message: String? = null,
    val throwable: Throwable? = null,
    val isDataEmpty: Boolean = false,
    val currentPosition: Int = -1,
    val searchText: String? = null
)