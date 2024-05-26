package com.example.movieappdemo1.presentation.ui.screen.movielist

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappdemo1.domain.model.MovieModelResult
import com.example.movieappdemo1.domain.usecase.GetPopularMoviesUseCase
import com.example.movieappdemo1.presentation.util.NetworkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListScreenViewModel @Inject constructor(
    private val networkManager: NetworkManager,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase
) : ViewModel() {

    private var language = "en-US"
    var isLoading = mutableStateOf(false)

    private var page = 1
    private var totalPages = 0
    var currentPosition = mutableStateOf(0)

    var allMovieList = mutableStateListOf<MovieModelResult>()

    fun getPopularMovies() {
        if(networkManager.checkNetworkState()) {
            viewModelScope.launch {
                getPopularMoviesUseCase.execute(language, page)
                    .onStart {
                        isLoading.value = true
                    }
                    .onCompletion {
                        delay(500L)
                        isLoading.value = false
                    }
                    .collect {
                        delay(500L)
                        allMovieList.addAll(it.movieModelResults)
                        totalPages = it.totalPages
                        page = it.page
                        page++
                    }
            }
        } else {

        }
    }
}