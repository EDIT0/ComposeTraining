package com.my.presentation.screen.movielist.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.my.common.NetworkManager
import com.my.domain.model.base.RequestResult
import com.my.domain.usecase.GetPopularMoviesUseCase
import com.my.presentation.screen.base.viewmodel.BaseAndroidViewModel
import com.my.presentation.screen.movielist.intent.viewmodeltoview.MovieListUiEvent
import com.my.presentation.screen.movielist.intent.viewtoviewmodel.MovieListViewModelEvent
import com.my.presentation.screen.base.sideeffect.SideEffectEvent
import com.my.presentation.screen.movielist.intent.viewmodeltoview.MovieCountUiEvent
import com.my.presentation.screen.movielist.intent.viewmodeltoview.MovieListUiErrorEvent
import com.my.presentation.screen.movielist.state.MovieCountUiState
import com.my.presentation.screen.movielist.state.MovieListUiState
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

    private val movieListUiEventChannel = Channel<MovieListUiEvent>() // Movie List 관련 State 업데이트
    val movieListUiState: StateFlow<MovieListUiState> = movieListUiEventChannel.receiveAsFlow() // Movie List 관련 State
        .runningFold(MovieListUiState(), ::reduceMovieListUiState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, MovieListUiState())

    // Movie List 에러 처리 이벤트
    private val _movieListUiErrorEvent = Channel<MovieListUiErrorEvent>()
    val movieListUiErrorEvent = _movieListUiErrorEvent.receiveAsFlow()

    private val movieCountUiEventChannel = Channel<MovieCountUiEvent>() // Movie Count 관련 State 업데이트
    val movieCountUiState: StateFlow<MovieCountUiState> = movieCountUiEventChannel.receiveAsFlow() // Movie Count 관련 State
        .runningFold(MovieCountUiState(), ::reduceMovieCountUiState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, MovieCountUiState())

    // Side Effect 이벤트
    private val _sideEffects = Channel<SideEffectEvent>()
    val sideEffects = _sideEffects.receiveAsFlow()

    private val language = "en-US"

    private var page = 1
    private var totalPages = 0

    private fun getPopularMovies() {
        if(networkManager.checkNetworkState()) {
            viewModelScope.launch {
                movieListUiEventChannel.send(MovieListUiEvent.Loading(isLoading = true))
                getPopularMoviesUseCase.execute(language, page)
                    .onStart {
                        _sideEffects.send(SideEffectEvent.ShowToast("데이터 요청 시작"))
                        movieCountUiEventChannel.send(MovieCountUiEvent.UpdateTotalMovieCount(-1))
                        movieCountUiEventChannel.send(MovieCountUiEvent.UpdateCurrentMovieCount(-1))
                    }
                    .onCompletion {
                        _sideEffects.send(SideEffectEvent.ShowToast("데이터 요청 끝"))
                    }
                    .filter {
                        val errorCode = "${it.code}"
                        val errorMessage = it.message

                        if(it is RequestResult.Error) {
                            _movieListUiErrorEvent.send(MovieListUiErrorEvent.Fail(errorCode, errorMessage))
//                            movieListUiEventChannel.send(MovieListUiEvent.Fail(errorCode, errorMessage))
                        } else if(it is RequestResult.ConnectionError) {
                            _movieListUiErrorEvent.send(MovieListUiErrorEvent.Fail(errorCode, errorMessage))
//                            movieListUiEventChannel.send(MovieListUiEvent.Fail(errorCode, errorMessage))
                        }

                        if(it is RequestResult.DataEmpty) {
                            _movieListUiErrorEvent.send(MovieListUiErrorEvent.DataEmpty(true))
//                            movieListUiEventChannel.send(MovieListUiEvent.DataEmpty(isDataEmpty = true))
                        }

                        return@filter it is RequestResult.Success
                    }
                    .catch {
                        _movieListUiErrorEvent.send(MovieListUiErrorEvent.ExceptionHandle(throwable = it))
//                        movieListUiEventChannel.send(MovieListUiEvent.ExceptionHandle(throwable = it))
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

                        movieCountUiEventChannel.send(MovieCountUiEvent.UpdateTotalMovieCount(totalPages * 20))
                        movieCountUiEventChannel.send(MovieCountUiEvent.UpdateCurrentMovieCount(movieListUiState.value.movieList?.size?:0))
                    }
                movieListUiEventChannel.send(MovieListUiEvent.Loading(isLoading = false))
            }
        } else {

        }
    }

    private fun updateCurrentPosition(position: Int) {
        viewModelScope.launch {
            movieListUiEventChannel.send(MovieListUiEvent.UpdateCurrentPosition(position))
        }
    }

    // ViewModel 이벤트 발생
    // View에서 ViewModel 함수 호출 시 무조건 이 함수를 통하여 호출
    fun handleViewModelEvent(movieListViewModelEvent: MovieListViewModelEvent) {
        when (movieListViewModelEvent) {
            is MovieListViewModelEvent.GetPopularMovies -> {
                getPopularMovies()
            }
            is MovieListViewModelEvent.UpdateCurrentPosition -> {
                updateCurrentPosition(movieListViewModelEvent.position)
            }
        }
    }

    // ViewModel의 데이터 처리에 대한 State 변화를 View에게 알림
    private fun reduceMovieListUiState(movieListUiState: MovieListUiState, movieListUiEvent: MovieListUiEvent) : MovieListUiState {
        return when(movieListUiEvent){
            is MovieListUiEvent.Success -> {
                movieListUiState.copy(movieList = movieListUiEvent.movieList)
            }
//            is MovieListUiEvent.Fail -> {
//                movieListUiState.copy(code = movieListUiEvent.code, message = movieListUiEvent.message, throwable = null, isDataEmpty = false)
//            }
//            is MovieListUiEvent.ExceptionHandle -> {
//                movieListUiState.copy(code = null, message = null, throwable = movieListUiEvent.throwable, isDataEmpty = false)
//            }
            is MovieListUiEvent.Loading -> {
                movieListUiState.copy(isLoading = movieListUiEvent.isLoading)
            }
//            is MovieListUiEvent.DataEmpty -> {
//                movieListUiState.copy(code = null, message = null, isDataEmpty = movieListUiEvent.isDataEmpty)
//            }
            is MovieListUiEvent.UpdateCurrentPosition -> {
                movieListUiState.copy(currentPosition = movieListUiEvent.currentPosition)
            }
        }
    }

    // ViewModel의 데이터 처리에 대한 State 변화를 View에게 알림
    private fun reduceMovieCountUiState(movieCountUiState: MovieCountUiState, movieCountUiEvent: MovieCountUiEvent) : MovieCountUiState {
        return when(movieCountUiEvent) {
            is MovieCountUiEvent.UpdateTotalMovieCount -> {
                movieCountUiState.copy(totalMovieCount = movieCountUiEvent.totalMovieCount)
            }
            is MovieCountUiEvent.UpdateCurrentMovieCount -> {
                movieCountUiState.copy(currentMovieCount = movieCountUiEvent.currentMovieCount)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()

        movieListUiEventChannel.close()
        movieCountUiEventChannel.close()
        _movieListUiErrorEvent.close()
        _sideEffects.close()
    }
}