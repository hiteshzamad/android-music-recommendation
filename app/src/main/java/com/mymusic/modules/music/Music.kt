package com.mymusic.modules.music

data class Music(
    var id: Long,
    val path: String,
    var name: String,
    var artist: String,
    var image: String,
    var localPath: String? = null
)

fun MusicEntity.toMusic() = Music(id = id, path = path, name = name, artist = artist, image = image)