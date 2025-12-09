package com.my.book.library.feature.select_library.library_detail.state

import com.my.book.library.core.model.res.ResSearchBookLibrary
import com.my.book.library.core.resource.LibraryData

data class SelectLibraryListDetailUiState(
    val detailRegion: LibraryData.DetailRegion? = null,
    val library: ResSearchBookLibrary.ResponseData.LibraryWrapper? = null
)