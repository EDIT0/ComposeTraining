package com.my.domain.usecase

import com.my.domain.model.MovieModel
import com.my.domain.model.base.RequestResult
import com.my.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository
) {
    suspend fun execute(query : String, language : String, page : Int) : Flow<RequestResult<MovieModel>> {
        return moviesRepository.getSearchMovies(query, language, page)
    }
}