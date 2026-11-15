package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "crop_scans")
data class CropScanEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fieldId: Long = 0,
    val cropName: String,
    val timestamp: Long = System.currentTimeMillis(),
    val imageUri: String = "",
    val problemName: String,
    val confidencePercent: Int,
    val symptoms: String,
    val possibleCauses: String,
    val recommendedSteps: String,
    val prevention: String,
    val isSevere: Boolean = false,
    val plantPart: String = "Leaf",
    val healthStatus: String = "Possible Abnormality",
    val immediateAction: String = "",
    val whenToSeekExpert: String = "",
    val isQualityIssue: Boolean = false,
    val qualityMessage: String = ""
)
