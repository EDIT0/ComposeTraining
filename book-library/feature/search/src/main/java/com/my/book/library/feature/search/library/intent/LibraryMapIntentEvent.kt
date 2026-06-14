package com.my.book.library.feature.search.library.intent

import androidx.paging.PagingData
import com.my.book.library.core.model.res.ResBookDetail
import com.my.book.library.core.model.res.ResCheckBookAvailability
import com.my.book.library.core.model.res.ResLibraryBookData
import com.my.book.library.core.model.res.ResSearchBookHoldingLibrary
import com.my.book.library.core.resource.LibraryData

sealed interface LibraryMapViewModelEvent {
    data class RequestInit(val isbn: String): LibraryMapViewModelEvent
    data class UpdateRegion(val detailRegion: LibraryData.DetailRegion): LibraryMapViewModelEvent
    data class SelectMarker(val libCode: String?): LibraryMapViewModelEvent
    data class UpdateSheetOffsetRatio(val ratio: Float): LibraryMapViewModelEvent
    data object StartLocationUpdates : LibraryMapViewModelEvent
    data object RequestMoveToMyLocation : LibraryMapViewModelEvent
    data class RequestLibraryBookData(val libCode: String, val isbn13: String, val type: String) : LibraryMapViewModelEvent
}

sealed interface LibraryMapUiEvent {
    data class UpdateDetailRegion(val detailRegion: LibraryData.DetailRegion): LibraryMapUiEvent
    data class UpdateUserLocation(val latitude: Double, val longitude: Double): LibraryMapUiEvent
    data class UpdateHoldingLibraryList(val holdingLibraryList: PagingData<ResSearchBookHoldingLibrary.ResponseData.LibraryWrapper>?): LibraryMapUiEvent
    data class UpdateSelectedLibCode(val libCode: String?): LibraryMapUiEvent
    data class UpdateSheetOffsetRatio(val ratio: Float): LibraryMapUiEvent
    data class UpdateCheckBookAvailability(val resCheckBookAvailability: ResCheckBookAvailability?): LibraryMapUiEvent
    data class UpdateLibraryBookData(val resLibraryBookData: ResLibraryBookData?): LibraryMapUiEvent
    data class UpdateLibraryBookDataLoading(val isLoading: Boolean): LibraryMapUiEvent
    data class UpdateBookDetail(val resBookDetail: ResBookDetail?): LibraryMapUiEvent
}