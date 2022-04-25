package com.mymusic.config

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.mymusic.modules.account.AccountCollection
import com.mymusic.modules.music.MusicCollection
import com.mymusic.modules.musichistory.MusicHistoryCollection

class RemoteDatabase {

    private val database = Firebase.firestore
    private val auth = Firebase.auth

    init {
        database.firestoreSettings = firestoreSettings {
            isPersistenceEnabled = true
        }
    }

    fun accountCollection() = AccountCollection(database)
    fun musicHistoryCollection() = MusicHistoryCollection(database, auth.uid!!)
    fun musicCollection() = MusicCollection(database)
}

