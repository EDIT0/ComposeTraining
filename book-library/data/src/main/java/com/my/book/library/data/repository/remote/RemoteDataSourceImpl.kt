package com.my.book.library.data.repository.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.my.book.library.core.model.req.ReqSearchDetailRegionBookLibrary
import com.my.book.library.core.model.req.ReqSearchLibCodeBookLibrary
import com.my.book.library.core.model.req.ReqSearchRegionBookLibrary
import com.my.book.library.core.model.res.ResSearchBookLibrary
import com.my.book.library.data.api.ApiService
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

}