package com.my.book.library.feature.main.intent.home

import com.my.book.library.core.model.local.MyRegionAndLibrary
import com.my.book.library.core.model.res.ResHotTrend

sealed interface HomeViewModelEvent {
    data object GetMyLibraryInfo : HomeViewModelEvent
    data object GetHotTrend : HomeViewModelEvent
}

sealed interface HomeUiEvent {
    data class UpdateMyLibraryInfo(val myLibraryInfo: MyRegionAndLibrary?) : HomeUiEvent
    data class UpdateHotTrendBooks(val hotTrendBooks: List<ResHotTrend.ResponseData.ResultWrapper.ResultData.DocWrapper.DocData>) : HomeUiEvent
}
