package com.my.domain.usecase

import com.my.domain.model.MovieModel
import com.my.domain.model.base.RequestResult
import com.my.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPopularMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository
) {
    suspend fun execute(language : String, page : Int) : Flow<RequestResult<MovieModel>> {
        return moviesRepository.getPopularMovies(language, page)
    }
}