package com.my.book.library.domain.usecase

import androidx.paging.PagingData
import com.my.book.library.core.model.req.ReqSearchBookWithTitle
import com.my.book.library.core.model.res.ResSearchBook
import com.my.book.library.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchBookWithTitleUseCase @Inject constructor(
    val repository: Repository
) {
    suspend fun invokePaging(
        reqSearchBookWithTitle: ReqSearchBookWithTitle,
        onResponseData: (ResSearchBook.ResponseData) -> Unit = {}
    ): Flow<PagingData<ResSearchBook.ResponseData.BookWrapper>> {
        return repository.getSearchBookPaging(
            reqSearchBookWithTitle = reqSearchBookWithTitle,
            onResponseData = onResponseData
        )
    }
}