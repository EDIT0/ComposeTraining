package com.my.book.library.core.model.req

data class ReqSearchLibCodeBookLibrary(
    val pageNo: Int,
    val pageSize: Int,
    val libCode: Int
)