package com.my.book.library.data.repository.remote

import androidx.paging.PagingData
import com.my.book.library.core.model.req.ReqBookDetail
import com.my.book.library.core.model.req.ReqCheckBookAvailability
import com.my.book.library.core.model.req.ReqLibraryBookData
import com.my.book.library.core.model.req.ReqSearchBookHoldingLibrary
import com.my.book.library.core.model.req.ReqSearchBookWithTitle
import com.my.book.library.core.model.req.ReqSearchDetailRegionBookLibrary
import com.my.book.library.core.model.req.ReqSearchLibCodeBookLibrary
import com.my.book.library.core.model.req.ReqSearchRegionBookLibrary
import com.my.book.library.core.model.res.ResBookDetail
import com.my.book.library.core.model.res.ResCheckBookAvailability
import com.my.book.library.core.model.res.ResLibraryBookData
import com.my.book.library.core.model.res.ResSearchBook
import com.my.book.library.core.model.res.ResSearchBookHoldingLibrary
import com.my.book.library.core.model.res.ResSearchBookLibrary
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RemoteDataSource {

    suspend fun getSearchRegionBookLibrary(authToken: String, format: String, reqSearchRegionBookLibrary: ReqSearchRegionBookLibrary): Response<ResSearchBookLibrary>
    suspend fun getSearchDetailRegionBookLibrary(authToken: String, format: String, reqSearchDetailRegionBookLibrary: ReqSearchDetailRegionBookLibrary): Response<ResSearchBookLibrary>
    suspend fun getSearchDetailRegionBookLibraryPaging(authToken: String, format: String, reqSearchDetailRegionBookLibrary: ReqSearchDetailRegionBookLibrary): Flow<PagingData<ResSearchBookLibrary.ResponseData.LibraryWrapper>>
    suspend fun getSearchLibCodeBookLibrary(authToken: String, format: String, reqSearchLibCodeBookLibrary: ReqSearchLibCodeBookLibrary): Response<ResSearchBookLibrary>
    suspend fun getSearchBookWithTitlePaging(authToken: String, format: String, reqSearchBookWithTitle: ReqSearchBookWithTitle, onResponseData: (ResSearchBook.ResponseData) -> Unit): Flow<PagingData<ResSearchBook.ResponseData.BookWrapper>>
    suspend fun getBookDetail(authToken: String, format: String, reqBookDetail: ReqBookDetail): Response<ResBookDetail>
    suspend fun getSearchBookHoldingLibraryPaging(authToken: String, format: String, reqSearchBookHoldingLibrary: ReqSearchBookHoldingLibrary): Flow<PagingData<ResSearchBookHoldingLibrary.ResponseData.LibraryWrapper>>
    suspend fun getCheckBookAvailability(authToken: String, format: String, reqCheckBookAvailability: ReqCheckBookAvailability): Response<ResCheckBookAvailability>
    suspend fun getLibraryBookData(authToken: String, format: String, reqLibraryBookData: ReqLibraryBookData): Response<ResLibraryBookData>
}