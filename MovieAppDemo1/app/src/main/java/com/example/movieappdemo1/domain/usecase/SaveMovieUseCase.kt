package com.example.movieappdemo1.domain.usecase

import com.example.movieappdemo1.domain.model.MovieModelResult
import com.example.movieappdemo1.domain.repository.MoviesRepository


class SaveMovieUseCase(
    private val moviesRepository: MoviesRepository
) {
    suspend fun execute(movieModelResult: MovieModelResult) {
        return moviesRepository.saveMovie(movieModelResult)
    }
}