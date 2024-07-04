package com.my.presentation.screen.movielist.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.my.common.NetworkManager
import com.my.domain.model.base.RequestResult
import com.my.domain.usecase.GetPopularMoviesUseCase
import com.my.presentation.screen.base.viewmodel.BaseAndroidViewModel
import com.my.presentation.screen.movielist.intent.MovieListUiEvent
import com.my.presentation.screen.movielist.model.MovieListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    app: Application,
    networkManager: NetworkManager,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase
) : BaseAndroidViewModel(app, networkManager) {

    private val movieListUiEventChannel = Channel<MovieListUiEvent>()
    val movieListUiState: StateFlow<MovieListUiState> = movieListUiEventChannel.receiveAsFlow()
        .runningFold(MovieListUiState(), ::reduceMovieListUiState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, MovieListUiState())

    private val _sideEffects = Channel<String>()
    val sideEffects = _sideEffects.receiveAsFlow()

    private fun reduceMovieListUiState(movieListUiState: MovieListUiState, movieListUiEvent: MovieListUiEvent) : MovieListUiState {
        return when(movieListUiEvent){
            is MovieListUiEvent.Success -> {
                movieListUiState.copy(movieList = movieListUiEvent.movieList, code = null, message = null, throwable = null, isDataEmpty = false)
            }
            is MovieListUiEvent.Fail -> {
                movieListUiState.copy(code = movieListUiEvent.code, message = movieListUiEvent.message, throwable = null, isDataEmpty = false)
            }
            is MovieListUiEvent.ExceptionHandle -> {
                movieListUiState.copy(code = null, message = null, throwable = movieListUiEvent.throwable, isDataEmpty = false)
            }
            is MovieListUiEvent.Loading -> {
                movieListUiState.copy(isLoading = movieListUiEvent.isLoading, isDataEmpty = false)
            }
            is MovieListUiEvent.DataEmpty -> {
                movieListUiState.copy(isDataEmpty = true)
            }
            is MovieListUiEvent.UpdateCurrentPosition -> {
                movieListUiState.copy(currentPosition = currentPosition)
            }
        }
    }

    fun updateCurrentPosition(position: Int) {
        currentPosition = position
        viewModelScope.launch {
            movieListUiEventChannel.send(MovieListUiEvent.UpdateCurrentPosition(currentPosition))
        }
    }


    private val language = "en-US"

    private var page = 1
    private var totalPages = 0

    var currentPosition = -1

    fun getPopularMovies() {
        if(networkManager.checkNetworkState()) {
            viewModelScope.launch {
                movieListUiEventChannel.send(MovieListUiEvent.Loading(isLoading = true))
                getPopularMoviesUseCase.execute(language, page)
                    .onStart { }
                    .onCompletion { }
                    .filter {
                        val errorCode = "${it.code}${Math.random()}"?:""
                        val errorMessage = it.message

                        if(it is RequestResult.Error) {
                            movieListUiEventChannel.send(MovieListUiEvent.Fail(errorCode, errorMessage))
                        } else if(it is RequestResult.ConnectionError) {
                            movieListUiEventChannel.send(MovieListUiEvent.Fail(errorCode, errorMessage))
                        }

                        if(it is RequestResult.DataEmpty) {
                            movieListUiEventChannel.send(MovieListUiEvent.DataEmpty(isDataEmpty = true))
                        }

                        return@filter it is RequestResult.Success
                    }
                    .catch {

                    }
                    .map {
                        it.resultData
                    }
                    .collect {
                        delay(1000L)
                        Log.d("MYTAG", "collect: ${it}")
                        if(movieListUiState.value.movieList == null || movieListUiState.value.movieList!!.isEmpty()) {
                            movieListUiEventChannel.send(MovieListUiEvent.Success(movieList = it!!.movieModelResults))
                        } else {
                            val tmp = ArrayList(movieListUiState.value.movieList)
                            tmp.addAll(it!!.movieModelResults)
                            movieListUiEventChannel.send(MovieListUiEvent.Success(tmp))
                        }
                        totalPages = it.totalPages
                        page = it.page
                        page++
                    }
                movieListUiEventChannel.send(MovieListUiEvent.Loading(isLoading = false))
            }
        } else {

        }
    }
}