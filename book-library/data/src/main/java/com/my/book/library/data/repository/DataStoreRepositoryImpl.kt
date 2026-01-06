package com.my.book.library.data.repository

import com.my.book.library.core.common.Constant
import com.my.book.library.core.common.util.DataStoreUtil
import com.my.book.library.core.model.local.MyRegionAndLibrary
import com.my.book.library.core.model.network.RequestResult
import com.my.book.library.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    val dataStoreUtil: DataStoreUtil
): DataStoreRepository {

    override suspend fun setMyLibraryInfo(myRegionAndLibrary: MyRegionAndLibrary): Flow<RequestResult<Boolean>> {
        return flow {
            val isSuccess = dataStoreUtil.setJson(DataStoreUtil.MY_LIBRARY_INFO, myRegionAndLibrary)

            if(isSuccess) {
                emit(RequestResult.Success(true))
            } else {
                emit(RequestResult.Error(Constant.DATA_STORE_SET_FAILURE_CODE, ""))
            }
        }
    }

    override suspend fun getMyLibraryInfo(): Flow<RequestResult<MyRegionAndLibrary>> {
        return flow {
            val res = dataStoreUtil.getJson(DataStoreUtil.MY_LIBRARY_INFO, MyRegionAndLibrary::class.java)

            if(res != null) {
                emit(RequestResult.Success(res))
            } else {
                emit(RequestResult.Error(Constant.DATA_STORE_GET_FAILURE_CODE, ""))
            }
        }
    }
}