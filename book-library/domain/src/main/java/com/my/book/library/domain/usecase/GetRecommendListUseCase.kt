package com.my.book.library.domain.usecase

import com.my.book.library.core.model.network.RequestResult
import com.my.book.library.core.model.req.ReqRecommendList
import com.my.book.library.core.model.res.ResRecommendList
import com.my.book.library.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecommendListUseCase @Inject constructor(
    val repository: Repository
) {

    suspend fun invoke(
        reqRecommendList: ReqRecommendList
    ): Flow<RequestResult<ResRecommendList>> {
        return repository.getRecommendList(reqRecommendList = reqRecommendList)
    }

}
