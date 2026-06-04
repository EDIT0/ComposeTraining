package com.my.book.library.data.api

import com.my.book.library.core.common.Constant
import com.my.book.library.core.model.res.ResBookDetail
import com.my.book.library.core.model.res.ResSearchBook
import com.my.book.library.core.model.res.ResSearchBookHoldingLibrary
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

    /**
     * 도서 검색
     *
     * @param authKey
     * @param format
     * @param pageNo
     * @param pageSize
     * @param keyword 키워드 (다중선택가능)
     * @param title 책 제목
     * @return
     */
    @GET("${Constant.URL_PATH_API}/srchBooks")
    suspend fun getSearchBookWithKeyword(
        @Query("authKey") authKey: String,
        @Query("format") format: String = Constant.JSON,
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @Query("keyword") keyword: String,
        @Query("title") title: String
    ): Response<ResSearchBook>

    /**
     * 도서 상세 조회
     *
     * @param authKey
     * @param isbn13
     * @param loaninfoYN
     * @param format
     * @return
     */
    @GET("${Constant.URL_PATH_API}/srchDtlList")
    suspend fun getBookDetail(
        @Query("authKey") authKey: String,
        @Query("isbn13") isbn13: String,
        @Query("loaninfoYN") loaninfoYN: String = "N",
        @Query("format") format: String = Constant.JSON
    ): Response<ResBookDetail>

    /**
     * 도서 소장 도서관 조회
     *
     * @param authKey
     * @param isbn
     * @param region
     * @param dtlRegion
     * @param format
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GET("${Constant.URL_PATH_API}/libSrchByBook")
    suspend fun getSearchBookHoldingLibrary(
        @Query("authKey") authKey: String,
        @Query("isbn") isbn: String,
        @Query("region") region: Int,
        @Query("dtl_region") dtlRegion: Int,
        @Query("format") format: String = Constant.JSON,
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int
    ): Response<ResSearchBookHoldingLibrary>
}