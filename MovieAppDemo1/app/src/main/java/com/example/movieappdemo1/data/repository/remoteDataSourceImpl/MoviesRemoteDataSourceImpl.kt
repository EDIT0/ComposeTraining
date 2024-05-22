package com.example.movieappdemo1.data.repository.remoteDataSourceImpl

import com.example.movieappdemo1.BuildConfig
import com.example.movieappdemo1.data.api.TmdbAPIService
import com.example.movieappdemo1.data.repository.remoteDataSource.MoviesRemoteDataSource
import com.example.movieappdemo1.domain.model.MovieModel
import retrofit2.Response

class MoviesRemoteDataSourceImpl(
    private val tmdbAPIService: TmdbAPIService
) : MoviesRemoteDataSource {
    override suspend fun getPopularMovies(language: String, page: Int): Response<MovieModel> {
        return tmdbAPIService.getPopularMovies(BuildConfig.API_KEY, language, page)
    }

    override suspend fun getSearchMovies(query: String, language: String, page: Int): Response<MovieModel> {
        return tmdbAPIService.getSearchMovies(BuildConfig.API_KEY, query, language, page)
    }
}