package com.my.book.library.domain.usecase

import com.my.book.library.core.model.network.RequestResult
import com.my.book.library.core.model.req.ReqHotTrend
import com.my.book.library.core.model.res.ResHotTrend
import com.my.book.library.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetHotTrendUseCase @Inject constructor(
    val repository: Repository
) {

    suspend fun invoke(
        reqHotTrend: ReqHotTrend
    ): Flow<RequestResult<List<ResHotTrend.ResponseData.ResultWrapper.ResultData.DocWrapper.DocData>>> {
        return repository.getHotTrend(reqHotTrend = reqHotTrend).map { result ->
            when (result) {
                is RequestResult.Success -> RequestResult.Success(data = mergeHotTrendBooks(result.resultData))
                is RequestResult.DataEmpty -> RequestResult.DataEmpty()
                is RequestResult.Error -> RequestResult.Error(code = result.code ?: 0, message = result.message)
                is RequestResult.AuthError -> RequestResult.AuthError(code = result.code ?: 0, message = result.message)
                is RequestResult.ConnectionError -> RequestResult.ConnectionError(code = result.code, message = result.message)
                is RequestResult.Loading -> RequestResult.Loading()
            }
        }
    }

    // 검색일자 포함 최근 3일치 대출급상승 도서(일자별 최대 5권)를 한 리스트로 합치고 동일 ISBN 도서는 중복 제거
    private fun mergeHotTrendBooks(
        resHotTrend: ResHotTrend?
    ): List<ResHotTrend.ResponseData.ResultWrapper.ResultData.DocWrapper.DocData> {
        return resHotTrend?.response?.results
            .orEmpty()
            .mapNotNull { it.result }
            .flatMap { it.docs.orEmpty() }
            .mapNotNull { it.doc }
            .distinctBy { it.isbn13 }
    }

}
