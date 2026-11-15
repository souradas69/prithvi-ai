package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "community_posts")
data class CommunityPostEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val authorName: String,
    val location: String,
    val cropTag: String,
    val content: String,
    val imageUri: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val isLiked: Boolean = false,
    val isUserPost: Boolean = false
)
