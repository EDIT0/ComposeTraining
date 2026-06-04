package com.my.book.library.core.model.req

data class ReqSearchBookWithTitle(
    val pageNo: Int,
    val pageSize: Int,
    val keyword: String,
    val title: String
)
