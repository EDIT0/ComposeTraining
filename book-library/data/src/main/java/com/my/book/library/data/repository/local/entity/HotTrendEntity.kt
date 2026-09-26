package com.my.book.library.data.repository.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hot_trend")
data class HotTrendEntity(
    @PrimaryKey
    val searchDt: String,
    val responseJson: String
)
