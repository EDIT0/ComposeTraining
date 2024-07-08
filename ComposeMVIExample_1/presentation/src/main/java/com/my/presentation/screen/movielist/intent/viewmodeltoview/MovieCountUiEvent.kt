package com.my.presentation.screen.movielist.intent.viewmodeltoview

sealed interface MovieCountUiEvent {
    class UpdateTotalMovieCount(val totalMovieCount: Int) : MovieCountUiEvent
    class UpdateCurrentMovieCount(val currentMovieCount: Int) : MovieCountUiEvent
}