package com.my.book.library.domain.usecase

import com.my.book.library.core.model.network.RequestResult
import com.my.book.library.core.model.req.ReqSearchRegionBookLibrary
import com.my.book.library.core.model.res.ResSearchBookLibrary
import com.my.book.library.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchRegionBookLibraryUseCase @Inject constructor(
    val repository: Repository
) {

    suspend fun invoke(
        reqSearchRegionBookLibrary: ReqSearchRegionBookLibrary
    ): Flow<RequestResult<ResSearchBookLibrary>> {
        return repository.getSearchRegionBookLibrary(reqSearchRegionBookLibrary = reqSearchRegionBookLibrary)
    }

}