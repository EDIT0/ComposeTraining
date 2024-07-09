package com.my.domain.repository

import com.my.domain.model.MovieModel
import com.my.domain.model.base.RequestResult
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    suspend fun getPopularMovies(language : String, page : Int) : Flow<RequestResult<MovieModel>>
    suspend fun getSearchMovies(query : String, language: String, page: Int) : Flow<RequestResult<MovieModel>>
}