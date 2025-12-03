package com.my.book.library.feature.select_library.library.intent

import androidx.paging.PagingData
import com.my.book.library.core.model.res.ResSearchBookLibrary
import com.my.book.library.core.resource.LibraryData

sealed interface SelectLibraryListViewModelEvent {
    data class UpdateDetailRegion(val detailRegion: LibraryData.DetailRegion): SelectLibraryListViewModelEvent
    class RequestLibraryList: SelectLibraryListViewModelEvent
}

sealed interface SelectLibraryListUiEvent {
    data class UpdateDetailRegion(val detailRegion: LibraryData.DetailRegion): SelectLibraryListUiEvent
    data class UpdateLibraryList(val libraryList: PagingData<ResSearchBookLibrary.ResponseData.LibraryWrapper>?): SelectLibraryListUiEvent
}