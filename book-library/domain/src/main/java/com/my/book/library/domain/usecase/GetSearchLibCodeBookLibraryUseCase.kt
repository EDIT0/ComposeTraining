package com.my.book.library.domain.usecase

import com.my.book.library.core.model.network.RequestResult
import com.my.book.library.core.model.req.ReqSearchLibCodeBookLibrary
import com.my.book.library.core.model.res.ResSearchBookLibrary
import com.my.book.library.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchLibCodeBookLibraryUseCase @Inject constructor(
    val repository: Repository
) {

    suspend fun invoke(
        reqSearchLibCodeBookLibrary: ReqSearchLibCodeBookLibrary
    ): Flow<RequestResult<ResSearchBookLibrary>> {
        return repository.getSearchLibCodeBookLibrary(reqSearchLibCodeBookLibrary = reqSearchLibCodeBookLibrary)
    }

}