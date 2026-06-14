package com.my.book.library.core.model.req

data class ReqCheckBookAvailability(
    val libCode: Int,
    val isbn13: String
)
