package com.my.book.library.domain.usecase

import com.my.book.library.core.model.network.RequestResult
import com.my.book.library.core.model.req.ReqRecommandList
import com.my.book.library.core.model.res.ResRecommandList
import com.my.book.library.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecommandListUseCase @Inject constructor(
    val repository: Repository
) {

    suspend fun invoke(
        reqRecommandList: ReqRecommandList
    ): Flow<RequestResult<ResRecommandList>> {
        return repository.getRecommandList(reqRecommandList = reqRecommandList)
    }

}
