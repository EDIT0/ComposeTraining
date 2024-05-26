package com.example.movieappdemo1.domain.usecase

import com.example.movieappdemo1.domain.model.MovieModel
import com.example.movieappdemo1.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class GetPopularMoviesUseCase(
    private val moviesRepository: MoviesRepository
) {
    suspend fun execute(language : String, page : Int) : Flow<MovieModel> {
        return flow {
            if(moviesRepository.getPopularMovies(language, page).body() != null) {
                emit(moviesRepository.getPopularMovies(language, page).body()!!)
            } else {

            }
        }
    }
}