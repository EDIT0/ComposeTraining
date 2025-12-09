package com.my.book.library.feature.select_library.library_detail.intent

import com.my.book.library.core.model.res.ResSearchBookLibrary
import com.my.book.library.core.resource.LibraryData

sealed interface SelectLibraryListDetailViewModelEvent {
    data class UpdateDetailRegionAndLibrary(val detailRegion: LibraryData.DetailRegion, val library: ResSearchBookLibrary.ResponseData.LibraryWrapper): SelectLibraryListDetailViewModelEvent
}

sealed interface SelectLibraryListDetailUiEvent {
    data class UpdateDetailRegion(val detailRegion: LibraryData.DetailRegion): SelectLibraryListDetailUiEvent
    data class UpdateLibrary(val library: ResSearchBookLibrary.ResponseData.LibraryWrapper): SelectLibraryListDetailUiEvent
}