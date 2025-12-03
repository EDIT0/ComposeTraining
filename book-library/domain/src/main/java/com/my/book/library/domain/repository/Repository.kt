package com.my.book.library.domain.repository

import androidx.paging.PagingData
import com.my.book.library.core.model.network.RequestResult
import com.my.book.library.core.model.req.ReqSearchDetailRegionBookLibrary
import com.my.book.library.core.model.req.ReqSearchLibCodeBookLibrary
import com.my.book.library.core.model.req.ReqSearchRegionBookLibrary
import com.my.book.library.core.model.res.ResSearchBookLibrary
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun getSearchRegionBookLibrary(reqSearchRegionBookLibrary: ReqSearchRegionBookLibrary): Flow<RequestResult<ResSearchBookLibrary>>
    suspend fun getSearchDetailRegionBookLibrary(reqSearchDetailRegionBookLibrary: ReqSearchDetailRegionBookLibrary): Flow<RequestResult<ResSearchBookLibrary>>
    suspend fun getSearchDetailRegionBookLibraryPaging(reqSearchDetailRegionBookLibrary: ReqSearchDetailRegionBookLibrary): Flow<PagingData<ResSearchBookLibrary.ResponseData.LibraryWrapper>>
    suspend fun getSearchLibCodeBookLibrary(reqSearchLibCodeBookLibrary: ReqSearchLibCodeBookLibrary): Flow<RequestResult<ResSearchBookLibrary>>

}