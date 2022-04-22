package com.mymusic.modules.musichistory

data class MusicHistoryDocument(
    val name: String,
    val artist: String,
    val count: Long,
    val lastPlayed: Long
)
