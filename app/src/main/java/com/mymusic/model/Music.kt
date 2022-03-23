package com.mymusic.model

data class Music(
    val path: String = "",
    var name: String = "",
    var artist: String = "",
    var imageUri: String = "",
    var duration: Float = 0F
)
