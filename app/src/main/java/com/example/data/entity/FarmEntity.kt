package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "farms")
data class FarmEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val farmName: String,
    val location: String,
    val area: Double,
    val unit: String, // Acres, Hectares, Bigha, Gunta
    val soilType: String,
    val irrigationMethod: String, // Drip, Canal, Borewell, Rainfed, Sprinkler
    val waterSource: String,
    val farmingMethod: String, // Organic, Conventional, Integrated
    val season: String // Kharif, Rabi, Zaid, Year-round
)
