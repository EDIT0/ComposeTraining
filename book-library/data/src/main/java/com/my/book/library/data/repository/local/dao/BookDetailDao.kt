package com.my.book.library.data.repository.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.my.book.library.data.repository.local.entity.BookDetailEntity

@Dao
interface BookDetailDao {

    @Query("SELECT * FROM book_detail WHERE isbn13 = :isbn13 LIMIT 1")
    suspend fun getByIsbn13(isbn13: String): BookDetailEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: BookDetailEntity)
}
