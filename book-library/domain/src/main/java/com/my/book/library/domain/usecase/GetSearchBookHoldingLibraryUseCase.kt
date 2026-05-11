package com.my.book.library.domain.usecase

import androidx.paging.PagingData
import com.my.book.library.core.model.req.ReqSearchBookHoldingLibrary
import com.my.book.library.core.model.res.ResSearchBookHoldingLibrary
import com.my.book.library.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchBookHoldingLibraryUseCase @Inject constructor(
    val repository: Repository
) {

    suspend fun invokePaging(
        reqSearchBookHoldingLibrary: ReqSearchBookHoldingLibrary
    ): Flow<PagingData<ResSearchBookHoldingLibrary.ResponseData.LibraryWrapper>> {
        return repository.getSearchBookHoldingLibraryPaging(reqSearchBookHoldingLibrary = reqSearchBookHoldingLibrary)
    }

}
