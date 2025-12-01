package com.my.book.library.feature.select_library.detail_region.intent

import com.my.book.library.core.resource.LibraryData

sealed interface SelectLibraryDetailRegionViewModelEvent {
    data class UpdateRegion(val region: LibraryData.Region): SelectLibraryDetailRegionViewModelEvent
}

sealed interface SelectLibraryDetailRegionUiEvent {
    data class UpdateRegion(val region: LibraryData.Region): SelectLibraryDetailRegionUiEvent
}