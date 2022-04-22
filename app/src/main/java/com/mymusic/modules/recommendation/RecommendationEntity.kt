package com.mymusic.modules.recommendation

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recommendation")
data class RecommendationEntity(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "musicId") val musicId: Long
)
