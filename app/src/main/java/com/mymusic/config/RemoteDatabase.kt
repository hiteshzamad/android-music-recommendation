package com.mymusic.config

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.mymusic.modules.account.AccountCollection
import com.mymusic.modules.musichistory.MusicHistoryCollection

class RemoteDatabase {

    private val database: FirebaseFirestore = Firebase.firestore

    init {
        database.firestoreSettings = firestoreSettings {
            isPersistenceEnabled = true
        }
    }

    fun accountCollection() = AccountCollection(database)
    fun musicHistoryCollection() = MusicHistoryCollection(database)
}

