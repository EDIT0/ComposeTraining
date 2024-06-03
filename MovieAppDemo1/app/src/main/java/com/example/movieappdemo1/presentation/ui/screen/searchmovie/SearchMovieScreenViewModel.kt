package com.example.movieappdemo1.presentation.ui.screen.searchmovie

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappdemo1.domain.model.MovieModel
import com.example.movieappdemo1.domain.model.MovieModelResult
import com.example.movieappdemo1.domain.usecase.GetSearchMoviesUseCase
import com.example.movieappdemo1.presentation.util.NetworkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SearchMovieScreenViewModel @Inject constructor(
    private val networkManager: NetworkManager,
    private val getSearchMoviesUseCase: GetSearchMoviesUseCase
) : ViewModel() {

    val searchMovieSearchText = mutableStateOf("")

    val currentPosition = mutableStateOf(-1)

    private var language = "en-US"
    private var page = 1
    var isLoading = mutableStateOf(false)


    var searchedMovies = mutableStateListOf<MovieModelResult>()
    fun getSearchMovies(query : String, isClear: Boolean) {
        if(networkManager.checkNetworkState()) {
            if(isClear) {
                page = 1
                searchedMovies.clear()
            }

            viewModelScope.launch(Dispatchers.IO) {
                isLoading.value = true
                val apiResult = getSearchMoviesUseCase.execute(query, language, page)
                if(apiResult.body()?.movieModelResults.isNullOrEmpty()) {
                    searchedMovies.clear()
                } else {
                    page++
                    searchedMovies.addAll(apiResult.body()?.movieModelResults!!)
                }
                isLoading.value = false
            }
        }
    }
}