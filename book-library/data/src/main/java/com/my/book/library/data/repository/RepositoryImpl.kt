package com.my.book.library.data.repository

import androidx.paging.PagingData
import com.my.book.library.core.common.Constant
import com.my.book.library.core.model.network.RequestResult
import com.my.book.library.core.model.req.ReqSearchDetailRegionBookLibrary
import com.my.book.library.core.model.req.ReqSearchLibCodeBookLibrary
import com.my.book.library.core.model.req.ReqSearchRegionBookLibrary
import com.my.book.library.core.model.res.ResSearchBookLibrary
import com.my.book.library.data.BuildConfig
import com.my.book.library.data.repository.remote.RemoteDataSource
import com.my.book.library.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    val remoteDataSource: RemoteDataSource
): Repository {
    
    override suspend fun getSearchRegionBookLibrary(reqSearchRegionBookLibrary: ReqSearchRegionBookLibrary): Flow<RequestResult<ResSearchBookLibrary>> {
        return flow { 
            val response = remoteDataSource.getSearchRegionBookLibrary(
                authToken = BuildConfig.BOOK_LIBRARY_API_KEY,
                format = Constant.JSON,
                reqSearchRegionBookLibrary = reqSearchRegionBookLibrary
            )

            if(response.isSuccessful) {
                val body = response.body()
                if(body != null && body.response != null && body.response.libs.isNotEmpty()) {
                    emit(RequestResult.Success(data = body))
                } else {
                    emit(RequestResult.DataEmpty())
                }
            } else {
                emit(RequestResult.Error(code = response.code(), message = response.message()))
            }
        }
    }

    override suspend fun getSearchDetailRegionBookLibrary(reqSearchDetailRegionBookLibrary: ReqSearchDetailRegionBookLibrary): Flow<RequestResult<ResSearchBookLibrary>> {
        return flow {
            val response = remoteDataSource.getSearchDetailRegionBookLibrary(
                authToken = BuildConfig.BOOK_LIBRARY_API_KEY,
                format = Constant.JSON,
                reqSearchDetailRegionBookLibrary = reqSearchDetailRegionBookLibrary
            )

            if(response.isSuccessful) {
                val body = response.body()
                if(body != null && body.response != null && body.response.libs.isNotEmpty()) {
                    emit(RequestResult.Success(data = body))
                } else {
                    emit(RequestResult.DataEmpty())
                }
            } else {
                emit(RequestResult.Error(code = response.code(), message = response.message()))
            }
        }
    }

    override suspend fun getSearchDetailRegionBookLibraryPaging(reqSearchDetailRegionBookLibrary: ReqSearchDetailRegionBookLibrary): Flow<PagingData<ResSearchBookLibrary.ResponseData.LibraryWrapper>> {
        return remoteDataSource.getSearchDetailRegionBookLibraryPaging(
            authToken = BuildConfig.BOOK_LIBRARY_API_KEY,
            format = Constant.JSON,
            reqSearchDetailRegionBookLibrary = reqSearchDetailRegionBookLibrary
        )
    }

    override suspend fun getSearchLibCodeBookLibrary(reqSearchLibCodeBookLibrary: ReqSearchLibCodeBookLibrary): Flow<RequestResult<ResSearchBookLibrary>> {
        return flow {
            val response = remoteDataSource.getSearchLibCodeBookLibrary(
                authToken = BuildConfig.BOOK_LIBRARY_API_KEY,
                format = Constant.JSON,
                reqSearchLibCodeBookLibrary = reqSearchLibCodeBookLibrary
            )

            if(response.isSuccessful) {
                val body = response.body()
                if(body != null && body.response != null && body.response.libs.isNotEmpty()) {
                    emit(RequestResult.Success(data = body))
                } else {
                    emit(RequestResult.DataEmpty())
                }
            } else {
                emit(RequestResult.Error(code = response.code(), message = response.message()))
            }
        }
    }
    
}