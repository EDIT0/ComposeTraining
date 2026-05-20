package com.my.book.library.feature.search.library.intent

import androidx.paging.PagingData
import com.my.book.library.core.model.res.ResSearchBookLibrary
import com.my.book.library.core.resource.LibraryData

sealed interface LibraryMapViewModelEvent {
    data class RequestLibraryList(val detailRegion: LibraryData.DetailRegion): LibraryMapViewModelEvent
}

sealed interface LibraryMapUiEvent {
    data class UpdateLibraryList(val libraryList: PagingData<ResSearchBookLibrary.ResponseData.LibraryWrapper>?): LibraryMapUiEvent
}