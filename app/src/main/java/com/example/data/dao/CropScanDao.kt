package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.entity.CropScanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CropScanDao {
    @Query("SELECT * FROM crop_scans ORDER BY timestamp DESC")
    fun getAllScans(): Flow<List<CropScanEntity>>

    @Query("SELECT * FROM crop_scans WHERE fieldId = :fieldId ORDER BY timestamp DESC")
    fun getScansForField(fieldId: Long): Flow<List<CropScanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScan(scan: CropScanEntity): Long

    @Query("DELETE FROM crop_scans WHERE id = :scanId")
    suspend fun deleteScanById(scanId: Long)
}
