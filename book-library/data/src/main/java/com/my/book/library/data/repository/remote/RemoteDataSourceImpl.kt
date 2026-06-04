package com.my.book.library.data.repository.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.my.book.library.core.model.req.ReqBookDetail
import com.my.book.library.core.model.req.ReqSearchBookHoldingLibrary
import com.my.book.library.core.model.req.ReqSearchBookWithTitle
import com.my.book.library.core.model.req.ReqSearchDetailRegionBookLibrary
import com.my.book.library.core.model.req.ReqSearchLibCodeBookLibrary
import com.my.book.library.core.model.req.ReqSearchRegionBookLibrary
import com.my.book.library.core.model.res.ResBookDetail
import com.my.book.library.core.model.res.ResSearchBook
import com.my.book.library.core.model.res.ResSearchBookHoldingLibrary
import com.my.book.library.core.model.res.ResSearchBookLibrary
import com.my.book.library.data.api.ApiService
import com.my.book.library.data.repository.remote.paging.GetSearchBookHoldingLibraryPagingSource
import com.my.book.library.data.repository.remote.paging.GetSearchBookWithTitlePagingSource
import com.my.book.library.data.repository.remote.paging.GetSearchDetailRegionBookLibraryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService
): RemoteDataSource {

    override suspend fun getSearchRegionBookLibrary(
        authToken: String,
        format: String,
        reqSearchRegionBookLibrary: ReqSearchRegionBookLibrary
    ): Response<ResSearchBookLibrary> {
        return apiService.getSearchRegionBookLibrary(
            authKey = authToken,
            format = format,
            pageNo = reqSearchRegionBookLibrary.pageNo,
            pageSize = reqSearchRegionBookLibrary.pageSize,
            region = reqSearchRegionBookLibrary.region
        )
    }

    override suspend fun getSearchDetailRegionBookLibrary(
        authToken: String,
        format: String,
        reqSearchDetailRegionBookLibrary: ReqSearchDetailRegionBookLibrary
    ): Response<ResSearchBookLibrary> {
        return apiService.getSearchDetailRegionBookLibrary(
            authKey = authToken,
            format = format,
            pageNo = reqSearchDetailRegionBookLibrary.pageNo,
            pageSize = reqSearchDetailRegionBookLibrary.pageSize,
            dtlRegion = reqSearchDetailRegionBookLibrary.dtlRegion
        )
    }

    override suspend fun getSearchDetailRegionBookLibraryPaging(
        authToken: String,
        format: String,
        reqSearchDetailRegionBookLibrary: ReqSearchDetailRegionBookLibrary
    ): Flow<PagingData<ResSearchBookLibrary.ResponseData.LibraryWrapper>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 5,
                enablePlaceholders = false,
                initialLoadSize = 20,
                maxSize = 10000,
//                jumpThreshold =
            ),

            // 사용할 메소드 선언
            pagingSourceFactory = {
                GetSearchDetailRegionBookLibraryPagingSource(
                    apiService = apiService,
                    authToken = authToken,
                    format = format,
                    reqSearchDetailRegionBookLibrary = reqSearchDetailRegionBookLibrary
                )
            }
        ).flow.catch {
            throw Exception(it)
        }
    }

    override suspend fun getSearchLibCodeBookLibrary(
        authToken: String,
        format: String,
        reqSearchLibCodeBookLibrary: ReqSearchLibCodeBookLibrary
    ): Response<ResSearchBookLibrary> {
        return apiService.getSearchLibCodeBookLibrary(
            authKey = authToken,
            format = format,
            pageNo = reqSearchLibCodeBookLibrary.pageNo,
            pageSize = reqSearchLibCodeBookLibrary.pageSize,
            libCode = reqSearchLibCodeBookLibrary.libCode
        )
    }

    override suspend fun getSearchBookWithTitlePaging(
        authToken: String,
        format: String,
        reqSearchBookWithTitle: ReqSearchBookWithTitle,
        onResponseData: (ResSearchBook.ResponseData) -> Unit
    ): Flow<PagingData<ResSearchBook.ResponseData.BookWrapper>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 5,
                enablePlaceholders = false,
                initialLoadSize = 20,
                maxSize = 10000,
//                jumpThreshold =
            ),

            // 사용할 메소드 선언
            pagingSourceFactory = {
                GetSearchBookWithTitlePagingSource(
                    apiService = apiService,
                    authToken = authToken,
                    format = format,
                    reqSearchBookWithTitle = reqSearchBookWithTitle,
                    onResponseData = onResponseData
                )
            }
        ).flow.catch {
            throw Exception(it)
        }
    }

    override suspend fun getBookDetail(
        authToken: String,
        format: String,
        reqBookDetail: ReqBookDetail
    ): Response<ResBookDetail> {
        return apiService.getBookDetail(
            authKey = authToken,
            isbn13 = reqBookDetail.isbn13,
            loaninfoYN = reqBookDetail.loaninfoYN,
            format = format
        )
    }

    override suspend fun getSearchBookHoldingLibraryPaging(
        authToken: String,
        format: String,
        reqSearchBookHoldingLibrary: ReqSearchBookHoldingLibrary
    ): Flow<PagingData<ResSearchBookHoldingLibrary.ResponseData.LibraryWrapper>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 5,
                enablePlaceholders = false,
                initialLoadSize = 20,
                maxSize = 10000,
            ),
            pagingSourceFactory = {
                GetSearchBookHoldingLibraryPagingSource(
                    apiService = apiService,
                    authToken = authToken,
                    format = format,
                    reqSearchBookHoldingLibrary = reqSearchBookHoldingLibrary
                )
            }
        ).flow.catch {
            throw Exception(it)
        }
    }
}