package com.my.data.repository

import android.util.Log
import com.my.common.NetworkConstant
import com.my.data.repository.remoteDataSource.MoviesRemoteDataSource
import com.my.domain.model.MovieModel
import com.my.domain.model.base.RequestResult
import com.my.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject
import kotlin.random.Random

class MoviesRepositoryImpl @Inject constructor(
    private val moviesRemoteDataSource: MoviesRemoteDataSource,
) : MoviesRepository {

    override suspend fun getPopularMovies(
        language: String,
        page: Int
    ): Flow<RequestResult<MovieModel>> {
        return flow {
            val response = moviesRemoteDataSource.getPopularMovies(language, page)

            try {
                if(response.isSuccessful) {

                    val randomNumber = Random.nextInt(1, 100)
                    Log.d("MYTAG", "Random Number: ${randomNumber}")
                    if(randomNumber >= 40) {
                        if(response.body() != null && !response.body()?.movieModelResults.isNullOrEmpty()) {
                            emit(RequestResult.Success(response.body()!!))
                        } else {
                            emit(RequestResult.DataEmpty())
                        }
                    } else if(randomNumber >= 20) {
                        if(Random.nextInt(1, 20) >= 10) {
                            emit(RequestResult.Error(NetworkConstant.ERROR, "에러입니다."))
                        } else {
                            emit(RequestResult.Error(NetworkConstant.ERROR_2, "2 에러입니다."))
                        }
                    } else if(randomNumber >= 1) {
                        emit(RequestResult.DataEmpty())
                    }
                } else {
                    emit(RequestResult.ConnectionError(response.code().toString(), response.message()))
                }
            } catch (e: Exception) {
                throw Exception(e)
            }
        }
    }

    override suspend fun getSearchMovies(
        query: String,
        language: String,
        page: Int
    ): Response<MovieModel> {
        return moviesRemoteDataSource.getSearchMovies(query, language, page)
    }

}