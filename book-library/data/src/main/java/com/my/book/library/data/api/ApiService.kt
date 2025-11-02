package com.my.book.library.data.api

import com.my.book.library.core.common.Constant
import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.Query

//interface ApiService {
//
//    @GET("${Constant.URL_PATH_API}/libSrch")
//    suspend fun getSearchBookLibrary(
//        @Query("authKey") authKey: String,
//        @Query("pageNo") pageNo: Int,
//        @Query("pageSize") pageSize: Int,
//        @Query("format") format: String = Constant.JSON
//    ): Response<> // TODO DTO 만들어서 넣어야함.
//
//}