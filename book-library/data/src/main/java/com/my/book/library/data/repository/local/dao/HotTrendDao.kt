package com.my.book.library.data.repository.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.my.book.library.data.repository.local.entity.HotTrendEntity

@Dao
interface HotTrendDao {

    @Query("SELECT * FROM hot_trend WHERE searchDt = :searchDt LIMIT 1")
    suspend fun getBySearchDt(searchDt: String): HotTrendEntity?

    @Query("DELETE FROM hot_trend")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: HotTrendEntity)

    @Transaction
    suspend fun replace(entity: HotTrendEntity) {
        deleteAll()
        insert(entity)
    }
}
