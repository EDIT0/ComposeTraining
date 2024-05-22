package com.example.movieappdemo1.data.repository

import androidx.lifecycle.LiveData
import com.example.movieappdemo1.data.repository.localDataSource.MovieLocalDataSource
import com.example.movieappdemo1.data.repository.remoteDataSource.MoviesRemoteDataSource
import com.example.movieappdemo1.domain.model.MovieModel
import com.example.movieappdemo1.domain.model.MovieModelResult
import com.example.movieappdemo1.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class MoviesRepositoryImpl(
    private val moviesRemoteDataSource: MoviesRemoteDataSource,
    private val movieLocalDataSource: MovieLocalDataSource
) : MoviesRepository {
    override suspend fun getPopularMovies(
        language: String,
        page: Int
    ): Response<MovieModel> {
        return moviesRemoteDataSource.getPopularMovies(language, page)
    }

    override suspend fun getSearchMovies(
        query: String,
        language: String,
        page: Int
    ): Response<MovieModel> {
        return moviesRemoteDataSource.getSearchMovies(query, language, page)
    }

    override suspend fun saveMovie(movieModelResult: MovieModelResult) {
        movieLocalDataSource.saveMovie(movieModelResult)
    }

    override suspend fun deleteSavedMovie(movieModelResult: MovieModelResult) {
        movieLocalDataSource.deleteSavedMovie(movieModelResult)
    }

    override fun getSavedMovies(): Flow<List<MovieModelResult>> {
        return movieLocalDataSource.getSavedMovies()
    }

    override fun getSearchSavedMovies(keyword: String): LiveData<List<MovieModelResult>> {
        return movieLocalDataSource.getSearchSavedMovies(keyword)
    }

    override fun getSearchSavedMovies_using_stateflow(keyword: String): Flow<List<MovieModelResult>> {
        return movieLocalDataSource.getSearchSavedMovies_using_stateflow(keyword)
    }

}