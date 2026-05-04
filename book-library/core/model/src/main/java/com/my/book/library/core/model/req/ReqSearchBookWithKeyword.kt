package com.my.book.library.core.model.req

data class ReqSearchBookWithKeyword(
    val pageNo: Int,
    val pageSize: Int,
    val keyword: String
)
