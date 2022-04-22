package com.mymusic.modules.recommendation

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecommendationDao {

    @Query("SELECT * FROM recommendation")
    fun loadAll(): Flow<List<RecommendationEntity>>

    @Query("DELETE FROM recommendation")
    suspend fun deleteAll()

    @Insert
    suspend fun inserts(recommendationEntities: List<RecommendationEntity>)

}