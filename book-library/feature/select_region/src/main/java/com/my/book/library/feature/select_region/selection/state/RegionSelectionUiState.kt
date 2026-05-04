package com.my.book.library.feature.select_region.selection.state

import com.my.book.library.core.resource.LibraryData

data class RegionSelectionUiState(
    val region: LibraryData.Region? = null,
    val detailRegion: LibraryData.DetailRegion? = null
)