package com.my.book.library.domain.repository

import com.my.book.library.core.model.local.MyRegionAndLibrary
import com.my.book.library.core.model.network.RequestResult
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun setMyLibraryInfo(myRegionAndLibrary: MyRegionAndLibrary): Flow<RequestResult<Boolean>>
    suspend fun getMyLibraryInfo(): Flow<RequestResult<MyRegionAndLibrary>>
}