package com.mymusic.modules.musichistory

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "musicHistory")
data class MusicHistoryEntity(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "musicId") val musicId: Long,
    @ColumnInfo(name = "count") var count: Long,
    @ColumnInfo(name = "lastPlayed") var lastPlayed: Long,
)
