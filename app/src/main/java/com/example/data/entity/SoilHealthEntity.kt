package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "soil_health")
data class SoilHealthEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val farmId: Long = 1,
    val phLevel: Double, // e.g. 6.5
    val nitrogenKgHa: Double, // e.g. 240.0
    val phosphorusKgHa: Double, // e.g. 18.5
    val potassiumKgHa: Double, // e.g. 150.0
    val organicCarbonPercent: Double, // e.g. 0.55
    val testDate: String,
    val notes: String = ""
)
