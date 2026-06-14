package com.my.book.library.data.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.my.book.library.data.repository.local.dao.BookDetailDao
import com.my.book.library.data.repository.local.entity.BookDetailEntity

@Database(
    entities = [BookDetailEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDetailDao(): BookDetailDao
}
