package com.mymusic.model

data class Music(val path: String){
    lateinit var name: String
    lateinit var artist: String
    lateinit var imageUri: String
    var duration: Float = 0F
}
