package com.my.book.library.domain.usecase

import com.my.book.library.core.model.network.RequestResult
import com.my.book.library.core.model.req.ReqLibraryBookData
import com.my.book.library.core.model.res.ResLibraryBookData
import com.my.book.library.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLibraryBookDataUseCase @Inject constructor(
    val repository: Repository
) {

    suspend fun invoke(
        reqLibraryBookData: ReqLibraryBookData
    ): Flow<RequestResult<ResLibraryBookData>> {
        return repository.getLibraryBookData(reqLibraryBookData = reqLibraryBookData)
    }

}
