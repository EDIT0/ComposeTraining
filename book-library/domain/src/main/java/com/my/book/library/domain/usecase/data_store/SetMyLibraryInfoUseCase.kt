package com.my.book.library.domain.usecase.data_store

import com.my.book.library.core.model.local.MyRegionAndLibrary
import com.my.book.library.core.model.network.RequestResult
import com.my.book.library.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SetMyLibraryInfoUseCase @Inject constructor(
    val dataStoreRepository: DataStoreRepository
) {

    suspend fun invoke(myRegionAndLibrary: MyRegionAndLibrary): Flow<RequestResult<Boolean>> {
        return dataStoreRepository.setMyLibraryInfo(myRegionAndLibrary)
    }

}