package com.example.data.repository

import com.example.data.AppDatabase
import com.example.data.entity.CommunityPostEntity
import com.example.data.entity.CropScanEntity
import com.example.data.entity.ExpenseEntity
import com.example.data.entity.FarmEntity
import com.example.data.entity.FarmerProfileEntity
import com.example.data.entity.FieldEntity
import com.example.data.entity.HarvestEntity
import com.example.data.entity.SoilHealthEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class FarmerRepository(private val db: AppDatabase) {

    val farmerProfile: Flow<FarmerProfileEntity?> = db.farmerDao().getFarmerProfile()

    val allFarms: Flow<List<FarmEntity>> = db.farmFieldDao().getAllFarms()
    val allFields: Flow<List<FieldEntity>> = db.farmFieldDao().getAllFields()

    val allScans: Flow<List<CropScanEntity>> = db.cropScanDao().getAllScans()
    val allExpenses: Flow<List<ExpenseEntity>> = db.financeDao().getAllExpenses()
    val allHarvests: Flow<List<HarvestEntity>> = db.financeDao().getAllHarvests()
    val allSoilRecords: Flow<List<SoilHealthEntity>> = db.soilHealthDao().getAllSoilRecords()
    val allCommunityPosts: Flow<List<CommunityPostEntity>> = db.communityDao().getAllPosts()

    suspend fun saveProfile(profile: FarmerProfileEntity) {
        db.farmerDao().insertOrUpdateProfile(profile)
    }

    suspend fun addFarm(farm: FarmEntity): Long {
        return db.farmFieldDao().insertFarm(farm)
    }

    suspend fun updateFarm(farm: FarmEntity) {
        db.farmFieldDao().updateFarm(farm)
    }

    suspend fun deleteFarm(farmId: Long) {
        db.farmFieldDao().deleteFarmById(farmId)
    }

    suspend fun addField(field: FieldEntity): Long {
        return db.farmFieldDao().insertField(field)
    }

    suspend fun updateField(field: FieldEntity) {
        db.farmFieldDao().updateField(field)
    }

    suspend fun updateFieldLastWatered(fieldId: Long, date: Long) {
        db.farmFieldDao().updateFieldLastWatered(fieldId, date)
    }

    suspend fun deleteField(fieldId: Long) {
        db.farmFieldDao().deleteFieldById(fieldId)
    }

    suspend fun addCropScan(scan: CropScanEntity): Long {
        return db.cropScanDao().insertScan(scan)
    }

    suspend fun addExpense(expense: ExpenseEntity): Long {
        return db.financeDao().insertExpense(expense)
    }

    suspend fun deleteExpense(id: Long) {
        db.financeDao().deleteExpenseById(id)
    }

    suspend fun addHarvest(harvest: HarvestEntity): Long {
        return db.financeDao().insertHarvest(harvest)
    }

    suspend fun deleteHarvest(id: Long) {
        db.financeDao().deleteHarvestById(id)
    }

    suspend fun addSoilRecord(record: SoilHealthEntity): Long {
        return db.soilHealthDao().insertSoilRecord(record)
    }

    suspend fun addCommunityPost(post: CommunityPostEntity): Long {
        return db.communityDao().insertPost(post)
    }

    suspend fun toggleLikePost(post: CommunityPostEntity) {
        val newCount = if (post.isLiked) (post.likesCount - 1).coerceAtLeast(0) else post.likesCount + 1
        db.communityDao().updateLikeStatus(post.id, newCount, !post.isLiked)
    }

    suspend fun seedCommunityPostsIfEmpty() {
        val existing = db.communityDao().getAllPosts().firstOrNull()
        if (existing.isNullOrEmpty()) {
            db.communityDao().insertPost(
                CommunityPostEntity(
                    authorName = "Ramesh Yadav",
                    location = "Gorakhpur, Uttar Pradesh",
                    cropTag = "Tomato",
                    content = "Good results from organic farming! Using Jeevamrut and drip irrigation reduced my tomato leaf curl disease significantly.",
                    likesCount = 128,
                    commentsCount = 24,
                    isLiked = false
                )
            )
            db.communityDao().insertPost(
                CommunityPostEntity(
                    authorName = "Sita Devi",
                    location = "Patna, Bihar",
                    cropTag = "Wheat",
                    content = "Drip irrigation is saving 40% more water on my 3-acre wheat crop this season. Highly recommend checking soil moisture before watering.",
                    likesCount = 95,
                    commentsCount = 11,
                    isLiked = false
                )
            )
        }
    }

    suspend fun clearAllData() {
        db.farmerDao().clearProfile()
        // Database will reset on re-install or clear
    }
}
