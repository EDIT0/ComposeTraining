package com.my.book.library.feature.main.state.home

import com.my.book.library.core.model.local.MyRegionAndLibrary
import com.my.book.library.core.model.res.ResHotTrend
import com.my.book.library.core.model.res.ResLoanItemSrchByLib

data class HomeUiState(
    val myLibraryInfo: MyRegionAndLibrary? = null,
    val hotTrendBooks: List<ResHotTrend.ResponseData.ResultWrapper.ResultData.DocWrapper.DocData> = emptyList(),
    val popularLoanBooks: List<ResLoanItemSrchByLib.ResponseData.DocWrapper.DocData> = emptyList()
)