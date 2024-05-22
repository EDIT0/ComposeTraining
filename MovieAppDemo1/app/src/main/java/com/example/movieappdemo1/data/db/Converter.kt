package com.example.movieappdemo1.data.db

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun listToJson(value: List<Int>): Int? {
        return 0
    }

    @TypeConverter
    fun jsonToList(value: Int): List<Int> {
        return emptyList()
    }


//    @TypeConverter
//    fun toSource(number : Int):List<Int>{
//        return Source(name,name)
//    }
}