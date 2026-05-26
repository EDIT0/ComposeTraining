package com.my.book.library.feature.search.library.intent

import androidx.paging.PagingData
import com.my.book.library.core.model.res.ResSearchBookHoldingLibrary
import com.my.book.library.core.model.res.ResSearchBookLibrary
import com.my.book.library.core.resource.LibraryData

sealed interface LibraryMapViewModelEvent {
    data class RequestInit(val isbn: String): LibraryMapViewModelEvent
    data class UpdateRegion(val detailRegion: LibraryData.DetailRegion): LibraryMapViewModelEvent
    data class RequestHoldingLibraryList(val isbn: String, val region: Int, val dtlRegion: Int): LibraryMapViewModelEvent
}

sealed interface LibraryMapUiEvent {
    data class UpdateDetailRegion(val detailRegion: LibraryData.DetailRegion): LibraryMapUiEvent
    data class UpdateUserLocation(val latitude: Double, val longitude: Double): LibraryMapUiEvent
    data class UpdateLibraryList(val libraryList: PagingData<ResSearchBookLibrary.ResponseData.LibraryWrapper>?): LibraryMapUiEvent
    data class UpdateHoldingLibraryList(val holdingLibraryList: PagingData<ResSearchBookHoldingLibrary.ResponseData.LibraryWrapper>?): LibraryMapUiEvent
}