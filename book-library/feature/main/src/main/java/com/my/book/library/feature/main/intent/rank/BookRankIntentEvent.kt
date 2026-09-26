package com.my.book.library.feature.main.intent.rank

import androidx.paging.PagingData
import com.my.book.library.core.model.local.BookRankType
import com.my.book.library.core.model.res.ResHotTrend
import com.my.book.library.core.model.res.ResLoanItemSrchByLib

sealed interface BookRankViewModelEvent {
    data class LoadBookRank(val bookRankType: BookRankType) : BookRankViewModelEvent
}

sealed interface BookRankUiEvent {
    data class UpdateBookRankType(val bookRankType: BookRankType) : BookRankUiEvent
    data class UpdateHotTrendBooks(val hotTrendBooks: List<ResHotTrend.ResponseData.ResultWrapper.ResultData.DocWrapper.DocData>) : BookRankUiEvent
    data class UpdatePopularLoanBooks(val popularLoanBooks: PagingData<ResLoanItemSrchByLib.ResponseData.DocWrapper>) : BookRankUiEvent
}
