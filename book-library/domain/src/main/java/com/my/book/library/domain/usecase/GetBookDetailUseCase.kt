package com.my.book.library.domain.usecase

import com.my.book.library.core.model.network.RequestResult
import com.my.book.library.core.model.req.ReqBookDetail
import com.my.book.library.core.model.res.ResBookDetail
import com.my.book.library.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBookDetailUseCase @Inject constructor(
    val repository: Repository
) {

    suspend fun invoke(
        reqBookDetail: ReqBookDetail
    ): Flow<RequestResult<ResBookDetail>> {
        return repository.getBookDetail(reqBookDetail = reqBookDetail)
    }

}
