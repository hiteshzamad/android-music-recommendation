package com.mymusic.modules.music

data class Music(
    var id: String,
    val path: String,
    var name: String,
    var artist: String,
    var image: String,
    var localPath: String? = null
)