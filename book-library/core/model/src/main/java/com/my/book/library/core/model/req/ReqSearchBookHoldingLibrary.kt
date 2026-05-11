package com.my.book.library.core.model.req

data class ReqSearchBookHoldingLibrary(
    val isbn: String,
    val region: Int,
    val dtlRegion: Int,
    val pageNo: Int,
    val pageSize: Int
)
