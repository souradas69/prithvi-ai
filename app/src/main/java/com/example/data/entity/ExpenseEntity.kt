package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val farmId: Long,
    val category: String, // Seeds, Fertilizer, Labour, Water, Electricity, Machinery, Transport, Other
    val amount: Double,
    val note: String,
    val timestamp: Long = System.currentTimeMillis()
)
