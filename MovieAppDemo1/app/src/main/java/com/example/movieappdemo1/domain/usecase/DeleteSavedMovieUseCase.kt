package com.example.movieappdemo1.domain.usecase

import com.example.movieappdemo1.domain.model.MovieModelResult
import com.example.movieappdemo1.domain.repository.MoviesRepository

class DeleteSavedMovieUseCase(
    private val moviesRepository: MoviesRepository
) {
    suspend fun execute(movieModelResult: MovieModelResult) {
        moviesRepository.deleteSavedMovie(movieModelResult)
    }
}