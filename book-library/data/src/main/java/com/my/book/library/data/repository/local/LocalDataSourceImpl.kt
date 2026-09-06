package com.my.book.library.data.repository.local

import com.google.gson.Gson
import com.my.book.library.core.model.res.ResBookDetail
import com.my.book.library.core.model.res.ResHotTrend
import com.my.book.library.data.repository.local.dao.BookDetailDao
import com.my.book.library.data.repository.local.dao.HotTrendDao
import com.my.book.library.data.repository.local.entity.BookDetailEntity
import com.my.book.library.data.repository.local.entity.HotTrendEntity
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val bookDetailDao: BookDetailDao,
    private val hotTrendDao: HotTrendDao,
    private val gson: Gson
) : LocalDataSource {

    override suspend fun getBookDetail(isbn13: String): ResBookDetail? {
        return bookDetailDao.getByIsbn13(isbn13)?.let { entity ->
            gson.fromJson(entity.responseJson, ResBookDetail::class.java)
        }
    }

    override suspend fun saveBookDetail(isbn13: String, resBookDetail: ResBookDetail) {
        bookDetailDao.insert(
            BookDetailEntity(
                isbn13 = isbn13,
                responseJson = gson.toJson(resBookDetail)
            )
        )
    }

    override suspend fun getHotTrend(searchDt: String): ResHotTrend? {
        return hotTrendDao.getBySearchDt(searchDt)?.let { entity ->
            gson.fromJson(entity.responseJson, ResHotTrend::class.java)
        }
    }

    override suspend fun saveHotTrend(searchDt: String, resHotTrend: ResHotTrend) {
        hotTrendDao.replace(
            HotTrendEntity(
                searchDt = searchDt,
                responseJson = gson.toJson(resHotTrend)
            )
        )
    }
}
