package com.my.book.library.core.model.local

import com.my.book.library.core.model.res.ResSearchBookLibrary
import com.my.book.library.core.resource.LibraryData

data class MyRegionAndLibrary(
    val detailRegion: LibraryData.DetailRegion,
    val library: ResSearchBookLibrary.ResponseData.LibraryWrapper
)