package com.my.presentation.screen.moviesearch.intent.viewmodeltoview

import com.my.domain.model.MovieModel

sealed interface SearchAndListUiEvent {
    class Success(val searchedMovieModel: MovieModel) : SearchAndListUiEvent
    class Loading(val isLoading: Boolean) : SearchAndListUiEvent
    class UpdateSearchText(val searchText: String) : SearchAndListUiEvent
    class UpdateCurrentPosition(val currentPosition: Int) : SearchAndListUiEvent
    class UpdateIsPagingDone(val isPagingDone: Boolean) : SearchAndListUiEvent
}