package com.example.movieappdemo1.domain.usecase

import com.example.movieappdemo1.domain.model.MovieModel
import com.example.movieappdemo1.domain.repository.MoviesRepository
import retrofit2.Response

class GetPopularMoviesUseCase(
    private val moviesRepository: MoviesRepository
) {
    suspend fun execute(language : String, page : Int) : Response<MovieModel> {
        return moviesRepository.getPopularMovies(language, page)
    }
}