package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fields")
data class FieldEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val farmId: Long,
    val fieldName: String,
    val area: Double,
    val cropName: String,
    val cropVariety: String,
    val plantingDateEpochDays: Long,
    val growthStage: String, // Germination, Vegetative, Flowering, Fruiting, Harvest
    val soilInfo: String,
    val irrigationMethod: String,
    val waterSource: String,
    val lastWateredEpochDays: Long = 0,
    val notes: String = ""
)
