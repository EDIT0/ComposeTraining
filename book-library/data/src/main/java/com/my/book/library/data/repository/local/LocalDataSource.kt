package com.my.book.library.data.repository.local

import com.my.book.library.core.model.res.ResBookDetail

interface LocalDataSource {
    suspend fun getBookDetail(isbn13: String): ResBookDetail?
    suspend fun saveBookDetail(isbn13: String, resBookDetail: ResBookDetail)
}
