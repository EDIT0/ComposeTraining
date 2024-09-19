package com.my.presentation.screen.moviesearch.state

import com.my.domain.model.MovieModel

data class SearchAndListUiState(
    var searchedMovieModel: MovieModel? = null,
    val isLoading: Boolean = false,
    val currentPosition: Int = -1,
    val searchText: String? = null,
    val isPagingDone: Boolean = false
)