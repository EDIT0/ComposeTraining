package com.my.book.feature.search_library.intent

import com.my.book.library.core.resource.LibraryData

sealed interface SearchLibraryViewModelEvent {
    data class RequestGtSearchRegionBookLibrary(val region: Int, val pageNo: Int, val pageSize: Int): SearchLibraryViewModelEvent
    data class SetIsFilterOpen(val isFilterOpen: Boolean): SearchLibraryViewModelEvent
    data class SetSelectionRegion(val selectionRegion: LibraryData.Region?): SearchLibraryViewModelEvent
}

sealed interface SearchLibraryUiEvent {
    data class UpdateIsFilterOpen(val isFilterOpen: Boolean): SearchLibraryUiEvent
    data class UpdateSelectionRegion(val selectionRegion: LibraryData.Region?): SearchLibraryUiEvent
}