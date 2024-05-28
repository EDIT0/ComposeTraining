package com.example.movieappdemo1.presentation.ui.screen.savedmovie

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappdemo1.common.log.LogUtil
import com.example.movieappdemo1.domain.model.MovieModelResult
import com.example.movieappdemo1.domain.usecase.DeleteSavedMovieUseCase
import com.example.movieappdemo1.domain.usecase.GetSavedMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedMovieScreenViewModel @Inject constructor(
    private val getSavedMoviesUseCase: GetSavedMoviesUseCase,
    private val deleteSavedMovieUseCase: DeleteSavedMovieUseCase
) : ViewModel() {

    val savedMovieSearchText = mutableStateOf("")

    val allSavedMoviesList = getSavedMoviesUseCase.execute()
    val currentPosition = mutableStateOf(-1)

    fun deleteSavedMovie(movieModelResult: MovieModelResult) {
        LogUtil.i_dev("Delete ${movieModelResult.title}")
        viewModelScope.launch(Dispatchers.IO) {
            deleteSavedMovieUseCase.execute(movieModelResult)
        }
    }

}