package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "farmer_profile")
data class FarmerProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val phone: String,
    val language: String,
    val state: String,
    val district: String,
    val village: String,
    val onboardingCompleted: Boolean = false,
    val goals: String = "", // Comma-separated or JSON list of selected goals
    val uid: String = "",
    val email: String = "",
    val preferredLanguages: String = "English",
    val profilePhoto: String = "",
    val isAuthenticated: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
