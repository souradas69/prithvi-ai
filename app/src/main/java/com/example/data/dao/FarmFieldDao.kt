package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.FarmEntity
import com.example.data.entity.FieldEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FarmFieldDao {
    // Farm operations
    @Query("SELECT * FROM farms ORDER BY id ASC")
    fun getAllFarms(): Flow<List<FarmEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFarm(farm: FarmEntity): Long

    @Update
    suspend fun updateFarm(farm: FarmEntity)

    @Query("DELETE FROM farms WHERE id = :farmId")
    suspend fun deleteFarmById(farmId: Long)

    // Field operations
    @Query("SELECT * FROM fields ORDER BY id ASC")
    fun getAllFields(): Flow<List<FieldEntity>>

    @Query("SELECT * FROM fields WHERE farmId = :farmId ORDER BY id ASC")
    fun getFieldsForFarm(farmId: Long): Flow<List<FieldEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertField(field: FieldEntity): Long

    @Update
    suspend fun updateField(field: FieldEntity)

    @Query("UPDATE fields SET lastWateredEpochDays = :wateredDate WHERE id = :fieldId")
    suspend fun updateFieldLastWatered(fieldId: Long, wateredDate: Long)

    @Query("DELETE FROM fields WHERE id = :fieldId")
    suspend fun deleteFieldById(fieldId: Long)
}
