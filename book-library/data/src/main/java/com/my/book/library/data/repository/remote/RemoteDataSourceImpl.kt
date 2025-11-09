package com.my.book.library.data.repository.remote

import com.my.book.library.core.model.req.ReqSearchDetailRegionBookLibrary
import com.my.book.library.core.model.req.ReqSearchLibCodeBookLibrary
import com.my.book.library.core.model.req.ReqSearchRegionBookLibrary
import com.my.book.library.core.model.res.ResSearchBookLibrary
import com.my.book.library.data.api.ApiService
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