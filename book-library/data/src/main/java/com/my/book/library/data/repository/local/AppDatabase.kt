package com.my.book.library.data.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.my.book.library.data.repository.local.dao.BookDetailDao
import com.my.book.library.data.repository.local.dao.HotTrendDao
import com.my.book.library.data.repository.local.entity.BookDetailEntity
import com.my.book.library.data.repository.local.entity.HotTrendEntity

@Database(
    entities = [BookDetailEntity::class, HotTrendEntity::class],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDetailDao(): BookDetailDao
    abstract fun hotTrendDao(): HotTrendDao
}
