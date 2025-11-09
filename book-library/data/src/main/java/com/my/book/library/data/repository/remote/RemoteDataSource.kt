package com.my.book.library.data.repository.remote

import com.my.book.library.core.model.req.ReqSearchDetailRegionBookLibrary
import com.my.book.library.core.model.req.ReqSearchLibCodeBookLibrary
import com.my.book.library.core.model.req.ReqSearchRegionBookLibrary
import com.my.book.library.core.model.res.ResSearchBookLibrary
import retrofit2.Response

interface RemoteDataSource {

    suspend fun getSearchRegionBookLibrary(authToken: String, format: String, reqSearchRegionBookLibrary: ReqSearchRegionBookLibrary): Response<ResSearchBookLibrary>
    suspend fun getSearchDetailRegionBookLibrary(authToken: String, format: String, reqSearchDetailRegionBookLibrary: ReqSearchDetailRegionBookLibrary): Response<ResSearchBookLibrary>
    suspend fun getSearchLibCodeBookLibrary(authToken: String, format: String, reqSearchLibCodeBookLibrary: ReqSearchLibCodeBookLibrary): Response<ResSearchBookLibrary>
}