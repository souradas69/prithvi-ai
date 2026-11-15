package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.entity.ExpenseEntity
import com.example.data.entity.HarvestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FinanceDao {
    @Query("SELECT * FROM expenses ORDER BY timestamp DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity): Long

    @Query("DELETE FROM expenses WHERE id = :id")
    suspend fun deleteExpenseById(id: Long)

    @Query("SELECT * FROM harvests ORDER BY timestamp DESC")
    fun getAllHarvests(): Flow<List<HarvestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHarvest(harvest: HarvestEntity): Long

    @Query("DELETE FROM harvests WHERE id = :id")
    suspend fun deleteHarvestById(id: Long)
}
