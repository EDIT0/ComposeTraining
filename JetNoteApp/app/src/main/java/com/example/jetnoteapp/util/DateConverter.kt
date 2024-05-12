package com.example.jetnoteapp.util

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {
    @TypeConverter
    fun timestampToDate(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }
}