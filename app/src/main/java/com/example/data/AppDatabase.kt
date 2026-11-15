package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.CommunityDao
import com.example.data.dao.CropScanDao
import com.example.data.dao.FarmFieldDao
import com.example.data.dao.FarmerDao
import com.example.data.dao.FinanceDao
import com.example.data.dao.SoilHealthDao
import com.example.data.entity.CommunityPostEntity
import com.example.data.entity.CropScanEntity
import com.example.data.entity.ExpenseEntity
import com.example.data.entity.FarmEntity
import com.example.data.entity.FarmerProfileEntity
import com.example.data.entity.FieldEntity
import com.example.data.entity.HarvestEntity
import com.example.data.entity.SoilHealthEntity

@Database(
    entities = [
        FarmerProfileEntity::class,
        FarmEntity::class,
        FieldEntity::class,
        CropScanEntity::class,
        ExpenseEntity::class,
        HarvestEntity::class,
        SoilHealthEntity::class,
        CommunityPostEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun farmerDao(): FarmerDao
    abstract fun farmFieldDao(): FarmFieldDao
    abstract fun cropScanDao(): CropScanDao
    abstract fun financeDao(): FinanceDao
    abstract fun soilHealthDao(): SoilHealthDao
    abstract fun communityDao(): CommunityDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "prithvi_ai_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
