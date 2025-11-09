package com.my.book.library.data.api

import com.my.book.library.core.common.Constant
import com.my.book.library.core.model.res.ResSearchBookLibrary
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    /**
     * 정보공개 도서관 조회 (지역)
     *
     * @param authKey
     * @param format
     * @param pageNo
     * @param pageSize
     * @param region 지역
     * @return
     */
    @GET("${Constant.URL_PATH_API}/libSrch")
    suspend fun getSearchRegionBookLibrary(
        @Query("authKey") authKey: String,
        @Query("format") format: String = Constant.JSON,
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @Query("region") region: Int
    ): Response<ResSearchBookLibrary>

    /**
     * 정보공개 도서관 조회 (세부지역)
     *
     * @param authKey
     * @param format
     * @param pageNo
     * @param pageSize
     * @param dtlRegion 세부지역
     * @return
     */
    @GET("${Constant.URL_PATH_API}/libSrch")
    suspend fun getSearchDetailRegionBookLibrary(
        @Query("authKey") authKey: String,
        @Query("format") format: String = Constant.JSON,
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @Query("dtl_region") dtlRegion: Int
    ): Response<ResSearchBookLibrary>

    /**
     * 정보공개 도서관 조회 (도서관 코드)
     *
     * @param authKey
     * @param format
     * @param pageNo
     * @param pageSize
     * @param libCode 도서관 코드
     * @return
     */
    @GET("${Constant.URL_PATH_API}/libSrch")
    suspend fun getSearchLibCodeBookLibrary(
        @Query("authKey") authKey: String,
        @Query("format") format: String = Constant.JSON,
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @Query("libCode") libCode: Int
    ): Response<ResSearchBookLibrary>

}