package com.mymusic.modules.music

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "music")
data class MusicEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "artist") val artist: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "path") val path: String
)

fun Music.toMusicEntity() =
    MusicEntity(id, name, artist, image, path)