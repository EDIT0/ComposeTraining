package com.example.movieappdemo1.data.repository.localDataSource

import androidx.lifecycle.LiveData
import com.example.movieappdemo1.domain.model.MovieModelResult
import kotlinx.coroutines.flow.Flow

interface MovieLocalDataSource {

    suspend fun saveMovie(movieModelResult: MovieModelResult)
    fun getSavedMovies() : Flow<List<MovieModelResult>>
    suspend fun deleteSavedMovie(movieModelResult : MovieModelResult)
    fun getSearchSavedMovies(keyword : String) : LiveData<List<MovieModelResult>>
    fun getSearchSavedMovies_using_stateflow(keyword : String) : Flow<List<MovieModelResult>>
}