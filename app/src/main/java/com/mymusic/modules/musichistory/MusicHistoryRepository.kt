package com.mymusic.modules.musichistory

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mymusic.AppContainer
import com.mymusic.datasource.RemoteDatabase

class MusicHistoryRepository(
    private val auth: FirebaseAuth = Firebase.auth,
    private val remoteDatabase: RemoteDatabase = AppContainer.remoteDatabase
) {
    suspend fun getListenHistory() = remoteDatabase.getListenHistory(auth.currentUser!!.uid)
}