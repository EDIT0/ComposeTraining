package com.my.book.library.feature.select_region.selection.intent

import com.my.book.library.core.resource.LibraryData

sealed interface RegionSelectionViewModelEvent {
    data class SetSelectionRegion(val region: LibraryData.Region): RegionSelectionViewModelEvent
    data class SetSelectionDetailRegion(val detailRegion: LibraryData.DetailRegion?): RegionSelectionViewModelEvent
}

sealed interface RegionSelectionUiEvent {
    data class UpdateSelectionRegion(val region: LibraryData.Region): RegionSelectionUiEvent
    data class UpdateSelectionDetailRegion(val detailRegion: LibraryData.DetailRegion?): RegionSelectionUiEvent
}