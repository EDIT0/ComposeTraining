package com.my.book.library.domain.usecase.data_store

import com.my.book.library.core.common.Constant
import com.my.book.library.core.model.local.MyRegionAndLibrary
import com.my.book.library.core.model.network.RequestResult
import com.my.book.library.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMyLibraryInfoUseCase @Inject constructor(
    val dataStoreRepository: DataStoreRepository
) {

    suspend fun invoke(): Flow<RequestResult<MyRegionAndLibrary>> {
        return dataStoreRepository.getMyLibraryInfo().map { result ->
            when (result) {
                is RequestResult.Success -> {
                    if (result.resultData != null) result
                    else RequestResult.Error(Constant.DATA_STORE_GET_FAILURE_CODE, "")
                }
                else -> result
            }
        }
    }

}