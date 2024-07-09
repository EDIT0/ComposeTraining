package com.my.presentation.screen.moviesearch.state

import com.my.domain.model.MovieModelResult

data class SearchAndListUiState(
    var searchedMovieList: List<MovieModelResult>? = null,
    val isLoading: Boolean = false,
    val currentPosition: Int = -1,
    val searchText: String? = null,
    val isPagingDone: Boolean = false
)