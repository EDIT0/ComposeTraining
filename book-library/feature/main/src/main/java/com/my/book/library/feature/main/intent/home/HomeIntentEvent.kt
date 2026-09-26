package com.my.book.library.feature.main.intent.home

import com.my.book.library.core.model.local.MyRegionAndLibrary
import com.my.book.library.core.model.res.ResHotTrend
import com.my.book.library.core.model.res.ResLoanItemSrchByLib

sealed interface HomeViewModelEvent {
    data object GetMyLibraryInfo : HomeViewModelEvent
    data object GetHotTrend : HomeViewModelEvent
    data object GetPopularLoanBooks : HomeViewModelEvent
}

sealed interface HomeUiEvent {
    data class UpdateMyLibraryInfo(val myLibraryInfo: MyRegionAndLibrary?) : HomeUiEvent
    data class UpdateHotTrendBooks(val hotTrendBooks: List<ResHotTrend.ResponseData.ResultWrapper.ResultData.DocWrapper.DocData>) : HomeUiEvent
    data class UpdatePopularLoanBooks(val popularLoanBooks: List<ResLoanItemSrchByLib.ResponseData.DocWrapper.DocData>) : HomeUiEvent
}
