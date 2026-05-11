package com.my.book.library.core.model.req

data class ReqBookDetail(
    val isbn13: String,
    val loaninfoYN: String = "N"
)
