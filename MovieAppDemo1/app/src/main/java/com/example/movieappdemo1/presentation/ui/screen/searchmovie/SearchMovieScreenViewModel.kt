package com.example.movieappdemo1.presentation.ui.screen.searchmovie

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappdemo1.common.log.LogUtil
import com.example.movieappdemo1.domain.model.MovieModelResult
import com.example.movieappdemo1.domain.usecase.GetSearchMoviesUseCase
import com.example.movieappdemo1.presentation.util.NetworkManager
import com.example.movieappdemo1.presentation.util.SearchDelayUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    fun searchLogic() {
        val isPass = SearchDelayUtil.onDelay {
            LogUtil.i_dev("MYTAG 라스트 검색어: ${searchMovieSearchText.value}")
//                    searchMovieScreenViewModel.getSearchMovies(searchMovieScreenViewModel.searchMovieSearchText.value, true)
            getSearchMovies(searchMovieSearchText.value, true)
//            searchMovieScreenViewModelPresenter(SearchMovieScreenViewModelPresenter.GetSearchMovies(searchMovieSearchText, true))
        }
        if(isPass) {
            LogUtil.i_dev("MYTAG 검색어: ${searchMovieSearchText.value}")
//                    searchMovieScreenViewModel.getSearchMovies(searchMovieScreenViewModel.searchMovieSearchText.value, true)
            getSearchMovies(searchMovieSearchText.value, true)
//            searchMovieScreenViewModelPresenter(SearchMovieScreenViewModelPresenter.GetSearchMovies(searchMovieSearchText, true))
        }
    }
}