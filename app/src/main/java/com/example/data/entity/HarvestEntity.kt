package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "harvests")
data class HarvestEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fieldId: Long,
    val cropName: String,
    val quantity: Double,
    val unit: String, // Quintal, Kg, Ton
    val sellingPricePerUnit: Double,
    val marketName: String,
    val revenue: Double,
    val timestamp: Long = System.currentTimeMillis()
)
