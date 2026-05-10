package com.my.book.library.feature.select_region.complete.intent

import com.my.book.library.core.resource.LibraryData

sealed interface RegionSelectionCompleteViewModelEvent {
    data class SetDetailRegion(val detailRegion: LibraryData.DetailRegion): RegionSelectionCompleteViewModelEvent
    object RegisterRegionAndLibrary: RegionSelectionCompleteViewModelEvent
}

sealed interface RegionSelectionCompleteUiEvent {
    data class UpdateDetailRegion(val detailRegion: LibraryData.DetailRegion): RegionSelectionCompleteUiEvent
}