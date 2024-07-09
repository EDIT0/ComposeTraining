package com.my.presentation.screen.moviesearch.intent.viewmodeltoview

import com.my.domain.model.MovieModelResult

sealed interface SearchAndListUiEvent {
    class Success(val searchedMovieList: List<MovieModelResult>) : SearchAndListUiEvent
    class Loading(val isLoading: Boolean) : SearchAndListUiEvent
    class UpdateSearchText(val searchText: String) : SearchAndListUiEvent
    class UpdateCurrentPosition(val currentPosition: Int) : SearchAndListUiEvent
    class UpdateIsPagingDone(val isPagingDone: Boolean) : SearchAndListUiEvent
}