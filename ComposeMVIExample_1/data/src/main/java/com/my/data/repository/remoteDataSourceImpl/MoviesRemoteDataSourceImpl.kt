package com.my.data.repository.remoteDataSourceImpl

import com.my.data.BuildConfig
import com.my.data.api.TmdbAPIService
import com.my.data.repository.remoteDataSource.MoviesRemoteDataSource
import com.my.domain.model.MovieModel
import retrofit2.Response
import javax.inject.Inject

class MoviesRemoteDataSourceImpl @Inject constructor(
    private val tmdbAPIService: TmdbAPIService
) : MoviesRemoteDataSource {
    override suspend fun getPopularMovies(language: String, page: Int): Response<MovieModel> {
        return tmdbAPIService.getPopularMovies(BuildConfig.API_KEY, language, page)
    }

    override suspend fun getSearchMovies(query: String, language: String, page: Int): Response<MovieModel> {
        return tmdbAPIService.getSearchMovies(BuildConfig.API_KEY, query, language, page)
    }
}