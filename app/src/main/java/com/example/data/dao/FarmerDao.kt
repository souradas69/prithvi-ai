package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.FarmerProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FarmerDao {
    @Query("SELECT * FROM farmer_profile WHERE id = 1 LIMIT 1")
    fun getFarmerProfile(): Flow<FarmerProfileEntity?>

    @Query("SELECT * FROM farmer_profile WHERE id = 1 LIMIT 1")
    suspend fun getFarmerProfileSync(): FarmerProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: FarmerProfileEntity)

    @Query("DELETE FROM farmer_profile")
    suspend fun clearProfile()
}
