package com.example.movieappdemo1.domain.usecase

import com.example.movieappdemo1.domain.model.MovieModelResult
import com.example.movieappdemo1.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow

class GetSavedMoviesUseCase(
    private val moviesRepository: MoviesRepository
) {
    fun execute() : Flow<List<MovieModelResult>>{
        return moviesRepository.getSavedMovies()
    }
}