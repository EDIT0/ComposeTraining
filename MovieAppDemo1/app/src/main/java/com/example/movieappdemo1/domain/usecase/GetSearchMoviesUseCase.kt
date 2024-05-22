package com.example.movieappdemo1.domain.usecase

import com.example.movieappdemo1.domain.model.MovieModel
import com.example.movieappdemo1.domain.repository.MoviesRepository
import retrofit2.Response

class GetSearchMoviesUseCase(
    private val moviesRepository: MoviesRepository
) {
    suspend fun execute(query : String, language : String, page : Int) : Response<MovieModel> {
        return moviesRepository.getSearchMovies(query, language, page)
    }
}