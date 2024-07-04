package com.my.presentation.screen.moviedetail.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.my.common.NetworkManager
import com.my.domain.model.MovieModelResult
import com.my.domain.usecase.GetPopularMoviesUseCase
import com.my.presentation.screen.base.viewmodel.BaseAndroidViewModel
import com.my.presentation.screen.moviedetail.intent.MovieDetailInfoUiEvent
import com.my.presentation.screen.moviedetail.model.MovieDetailInfoUiState
import com.my.presentation.screen.movielist.intent.MovieListUiEvent
import com.my.presentation.screen.movielist.model.MovieListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    app: Application,
    networkManager: NetworkManager
) : BaseAndroidViewModel(app, networkManager) {

    private val movieDetailInfoUiEventChannel = Channel<MovieDetailInfoUiEvent>()
    val movieDetailInfoUiState: StateFlow<MovieDetailInfoUiState> = movieDetailInfoUiEventChannel.receiveAsFlow()
        .runningFold(MovieDetailInfoUiState(), ::reduceMovieDetailInfoUiState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, MovieDetailInfoUiState())

    private val _sideEffects = Channel<String>()
    val sideEffects = _sideEffects.receiveAsFlow()

    private fun reduceMovieDetailInfoUiState(movieDetailInfoUiState: MovieDetailInfoUiState, movieDetailInfoUiEvent: MovieDetailInfoUiEvent) : MovieDetailInfoUiState {
        return when(movieDetailInfoUiEvent){
            is MovieDetailInfoUiEvent.UpdateMovieDetailInfo -> {
                movieDetailInfoUiState.copy(movieModelResult = movieDetailInfoUiEvent.movieModelResult)
            }
        }
    }

    fun updateMovieModelResult(movieModelResult: MovieModelResult) {
        viewModelScope.launch {
            movieDetailInfoUiEventChannel.send(MovieDetailInfoUiEvent.UpdateMovieDetailInfo(movieModelResult))
        }
    }

}