package com.my.presentation.screen.moviesearch.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.my.common.NetworkManager
import com.my.domain.model.base.RequestResult
import com.my.domain.usecase.GetSearchMoviesUseCase
import com.my.presentation.screen.base.sideeffect.SideEffectEvent
import com.my.presentation.screen.base.viewmodel.BaseAndroidViewModel
import com.my.presentation.screen.moviesearch.intent.viewmodeltoview.SearchAndListUiErrorEvent
import com.my.presentation.screen.moviesearch.intent.viewmodeltoview.SearchAndListUiEvent
import com.my.presentation.screen.moviesearch.intent.viewtoviewmodel.MovieSearchViewModelEvent
import com.my.presentation.screen.moviesearch.state.SearchAndListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
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
class MovieSearchViewModel @Inject constructor(
    app: Application,
    networkManager: NetworkManager,
    private val getSearchMoviesUseCase: GetSearchMoviesUseCase
) : BaseAndroidViewModel(app, networkManager){

    private val searchAndUiEventChannel = Channel<SearchAndListUiEvent>() // Searched Movie List 관련 State 업데이트
    val searchAndListUiState: StateFlow<SearchAndListUiState> = searchAndUiEventChannel.receiveAsFlow() // Searched Movie List 관련 State
        .runningFold(SearchAndListUiState(), ::reduceSearchAndListUiState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, SearchAndListUiState())

    // Searched Movie List 에러 처리 이벤트
    private val _searchAndListUiErrorEvent = Channel<SearchAndListUiErrorEvent>()
    val searchAndListUiErrorEvent = _searchAndListUiErrorEvent.receiveAsFlow()

    // Side Effect 이벤트
    private val _sideEffects = Channel<SideEffectEvent>()
    val sideEffects = _sideEffects.receiveAsFlow()

    private var language = "en-US"
    private var page = 1
    private var currentSearchText = "" // 현재 검색된 검색어


    private fun getSearchMovies(query : String, isClear: Boolean) {
        // 같은 검색어 검색 x
        if(currentSearchText == query && isClear) {
            viewModelScope.launch {
                _sideEffects.send(SideEffectEvent.ShowToast("같은 검색어 검색"))
            }
            return
        }

        if(networkManager.checkNetworkState()) {

            viewModelScope.launch(Dispatchers.IO) {
                if(isClear) {
                    page = 1
                    searchAndUiEventChannel.send(SearchAndListUiEvent.Success(searchedMovieList = emptyList()))
                }

                currentSearchText = searchAndListUiState.value.searchText?:""

                searchAndUiEventChannel.send(SearchAndListUiEvent.Loading(isLoading = true))
                getSearchMoviesUseCase.execute(query, language, page)
                    .onStart {
//                        _sideEffects.send(SideEffectEvent.ShowToast("데이터 요청 시작"))
                    }
                    .onCompletion {
//                        _sideEffects.send(SideEffectEvent.ShowToast("데이터 요청 끝"))
                    }
                    .filter {
                        val errorCode = "${it.code}"
                        val errorMessage = it.message

                        if(it is RequestResult.Error) {
                            _searchAndListUiErrorEvent.send(SearchAndListUiErrorEvent.Fail(errorCode, errorMessage))
                        } else if(it is RequestResult.ConnectionError) {
                            _searchAndListUiErrorEvent.send(SearchAndListUiErrorEvent.Fail(errorCode, errorMessage))
                        }

                        if(it is RequestResult.DataEmpty) {
                            searchAndUiEventChannel.send(SearchAndListUiEvent.UpdateIsPagingDone(isPagingDone = true))
                            _searchAndListUiErrorEvent.send(SearchAndListUiErrorEvent.DataEmpty(true))
                        }

                        return@filter it is RequestResult.Success
                    }
                    .catch {
                        _searchAndListUiErrorEvent.send(SearchAndListUiErrorEvent.ExceptionHandle(throwable = it))
                    }
                    .map {
                        it.resultData
                    }
                    .collect {
                        Log.d("MYTAG", "collect: ${it}")

                        page = it!!.page

                        if(page >= it.totalPages) {
                            searchAndUiEventChannel.send(SearchAndListUiEvent.UpdateIsPagingDone(isPagingDone = true))
                        } else {
                            searchAndUiEventChannel.send(SearchAndListUiEvent.UpdateIsPagingDone(isPagingDone = false))
                        }

                        if(it.movieModelResults.isNullOrEmpty()) {
                            if(isClear) {
                                searchAndUiEventChannel.send(SearchAndListUiEvent.Success(searchedMovieList = emptyList()))
                            }
                        } else {
                            page++

                            if(searchAndListUiState.value.searchedMovieList == null || searchAndListUiState.value.searchedMovieList!!.isEmpty()) {
                                searchAndUiEventChannel.send(SearchAndListUiEvent.Success(searchedMovieList = it.movieModelResults))
                            } else {
                                val tmp = ArrayList(searchAndListUiState.value.searchedMovieList)
                                tmp.addAll(it.movieModelResults)
                                searchAndUiEventChannel.send(SearchAndListUiEvent.Success(tmp))
                            }
                        }
                    }
                searchAndUiEventChannel.send(SearchAndListUiEvent.Loading(isLoading = false))
            }
        }
    }

    private fun updateCurrentPosition(position: Int) {
        viewModelScope.launch {
            searchAndUiEventChannel.send(SearchAndListUiEvent.UpdateCurrentPosition(position))
        }
    }

    private fun updateSearchText(text: String) {
        viewModelScope.launch {
            searchAndUiEventChannel.send(SearchAndListUiEvent.UpdateSearchText(text))
        }
    }

    // ViewModel 이벤트 발생
    // View에서 ViewModel 함수 호출 시 무조건 이 함수를 통하여 호출
    fun handleViewModelEvent(movieSearchViewModelEvent: MovieSearchViewModelEvent) {
        when (movieSearchViewModelEvent) {
            is MovieSearchViewModelEvent.GetSearchMovies -> {
                getSearchMovies(movieSearchViewModelEvent.query, movieSearchViewModelEvent.isClear)
            }
            is MovieSearchViewModelEvent.UpdateCurrentPosition -> {
                updateCurrentPosition(movieSearchViewModelEvent.position)
            }
            is MovieSearchViewModelEvent.UpdateSearchMovieSearchText -> {
                updateSearchText(movieSearchViewModelEvent.text)
            }
        }
    }


    // ViewModel의 데이터 처리에 대한 State 변화를 View에게 알림
    private fun reduceSearchAndListUiState(searchAndListUiState: SearchAndListUiState, searchAndListUiEvent: SearchAndListUiEvent) : SearchAndListUiState {
        return when(searchAndListUiEvent){
            is SearchAndListUiEvent.Success -> {
                searchAndListUiState.copy(searchedMovieList = searchAndListUiEvent.searchedMovieList)
            }
            is SearchAndListUiEvent.Loading -> {
                searchAndListUiState.copy(isLoading = searchAndListUiEvent.isLoading)
            }
            is SearchAndListUiEvent.UpdateCurrentPosition -> {
                searchAndListUiState.copy(currentPosition = searchAndListUiEvent.currentPosition)
            }
            is SearchAndListUiEvent.UpdateSearchText -> {
                searchAndListUiState.copy(searchText = searchAndListUiEvent.searchText)
            }
            is SearchAndListUiEvent.UpdateIsPagingDone -> {
                searchAndListUiState.copy(isPagingDone = searchAndListUiEvent.isPagingDone)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        searchAndUiEventChannel.close()
        _searchAndListUiErrorEvent.close()
        _sideEffects.close()
    }
}