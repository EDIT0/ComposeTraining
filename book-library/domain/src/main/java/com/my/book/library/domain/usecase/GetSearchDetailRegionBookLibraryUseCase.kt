package com.my.book.library.domain.usecase

import com.my.book.library.core.model.network.RequestResult
import com.my.book.library.core.model.req.ReqSearchDetailRegionBookLibrary
import com.my.book.library.core.model.res.ResSearchBookLibrary
import com.my.book.library.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchDetailRegionBookLibraryUseCase @Inject constructor(
    val repository: Repository
) {

    suspend fun invoke(
        reqSearchDetailRegionBookLibrary: ReqSearchDetailRegionBookLibrary
    ): Flow<RequestResult<ResSearchBookLibrary>> {
        return repository.getSearchDetailRegionBookLibrary(reqSearchDetailRegionBookLibrary = reqSearchDetailRegionBookLibrary)
    }

}