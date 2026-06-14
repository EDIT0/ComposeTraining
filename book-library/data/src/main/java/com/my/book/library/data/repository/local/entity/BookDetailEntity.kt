package com.my.book.library.data.repository.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book_detail")
data class BookDetailEntity(
    @PrimaryKey
    val isbn13: String,
    val responseJson: String
)
