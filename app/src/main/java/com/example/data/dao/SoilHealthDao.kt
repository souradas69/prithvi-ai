package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.entity.SoilHealthEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SoilHealthDao {
    @Query("SELECT * FROM soil_health ORDER BY id DESC")
    fun getAllSoilRecords(): Flow<List<SoilHealthEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSoilRecord(record: SoilHealthEntity): Long

    @Query("DELETE FROM soil_health WHERE id = :id")
    suspend fun deleteSoilRecordById(id: Long)
}
