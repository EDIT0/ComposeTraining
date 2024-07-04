package com.my.data.api

import com.my.domain.model.MovieModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbAPIService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") api_key : String,
        @Query("language") language : String,
        @Query("page") page : Int
    ) : Response<MovieModel>

    @GET("search/movie")
    suspend fun getSearchMovies(
        @Query("api_key") api_key : String,
        @Query("query") query: String,
        @Query("language") language : String,
        @Query("page") page : Int,
        @Query("include_adult") include_adult : Boolean = false
    ) : Response<MovieModel>
}