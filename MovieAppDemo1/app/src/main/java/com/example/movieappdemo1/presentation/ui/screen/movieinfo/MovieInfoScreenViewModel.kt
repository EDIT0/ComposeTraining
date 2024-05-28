package com.example.movieappdemo1.presentation.ui.screen.movieinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappdemo1.domain.model.MovieModelResult
import com.example.movieappdemo1.domain.usecase.SaveMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieInfoScreenViewModel @Inject constructor(
    private val saveMovieUseCase: SaveMovieUseCase
) : ViewModel() {

    fun saveMovie(movieModelResult: MovieModelResult) {
        viewModelScope.launch(Dispatchers.IO) {
            saveMovieUseCase.execute(movieModelResult)
        }
    }

}