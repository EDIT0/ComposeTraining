package com.my.book.library.domain.usecase

import androidx.paging.PagingData
import com.my.book.library.core.model.network.RequestResult
import com.my.book.library.core.model.req.ReqLoanItemSrchByLib
import com.my.book.library.core.model.res.ResLoanItemSrchByLib
import com.my.book.library.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLoanItemSrchByLibUseCase @Inject constructor(
    val repository: Repository
) {

    suspend fun invoke(
        reqLoanItemSrchByLib: ReqLoanItemSrchByLib
    ): Flow<RequestResult<ResLoanItemSrchByLib>> {
        return repository.getLoanItemSrchByLib(reqLoanItemSrchByLib = reqLoanItemSrchByLib)
    }

    suspend fun invokePaging(
        reqLoanItemSrchByLib: ReqLoanItemSrchByLib
    ): Flow<PagingData<ResLoanItemSrchByLib.ResponseData.DocWrapper>> {
        return repository.getLoanItemSrchByLibPaging(reqLoanItemSrchByLib = reqLoanItemSrchByLib)
    }

}
