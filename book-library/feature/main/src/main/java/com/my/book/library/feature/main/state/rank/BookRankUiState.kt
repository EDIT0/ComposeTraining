package com.my.book.library.feature.main.state.rank

import androidx.paging.PagingData
import com.my.book.library.core.model.local.BookRankType
import com.my.book.library.core.model.res.ResHotTrend
import com.my.book.library.core.model.res.ResLoanItemSrchByLib
import kotlinx.coroutines.flow.MutableStateFlow

data class BookRankUiState(
    val bookRankType: BookRankType? = null,
    val hotTrendBooks: List<ResHotTrend.ResponseData.ResultWrapper.ResultData.DocWrapper.DocData> = emptyList(),
    val popularLoanBooks: MutableStateFlow<PagingData<ResLoanItemSrchByLib.ResponseData.DocWrapper>>? = null
)
