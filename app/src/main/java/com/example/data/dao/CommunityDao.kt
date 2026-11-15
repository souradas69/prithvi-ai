package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.entity.CommunityPostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CommunityDao {
    @Query("SELECT * FROM community_posts ORDER BY timestamp DESC")
    fun getAllPosts(): Flow<List<CommunityPostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: CommunityPostEntity): Long

    @Query("UPDATE community_posts SET likesCount = :newCount, isLiked = :isLiked WHERE id = :postId")
    suspend fun updateLikeStatus(postId: Long, newCount: Int, isLiked: Boolean)

    @Query("DELETE FROM community_posts WHERE id = :postId")
    suspend fun deletePostById(postId: Long)
}
