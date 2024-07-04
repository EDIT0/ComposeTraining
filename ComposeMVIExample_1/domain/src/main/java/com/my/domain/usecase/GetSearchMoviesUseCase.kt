package com.my.domain.usecase

import com.my.domain.model.MovieModel
import com.my.domain.repository.MoviesRepository
import retrofit2.Response
import javax.inject.Inject

class GetSearchMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository
) {
    suspend fun execute(query : String, language : String, page : Int) : Response<MovieModel> {
        return moviesRepository.getSearchMovies(query, language, page)
    }
}