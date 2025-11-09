package com.my.book.library.core.model.req

data class ReqSearchRegionBookLibrary(
    val pageNo: Int,
    val pageSize: Int,
    val region: Int
)