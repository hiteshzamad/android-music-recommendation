package com.mymusic.modules.musichistory

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mymusic.AppContainer

class MusicHistoryRepository(
    private val auth: FirebaseAuth = Firebase.auth,
    private val musicHistoryCollection: MusicHistoryCollection = AppContainer.musicHistoryCollection
) {
    suspend fun getAll() = musicHistoryCollection.getAll(auth.currentUser!!.uid)

    suspend fun add(name: String) = musicHistoryCollection.add(auth.currentUser!!.uid, name.trim())
}