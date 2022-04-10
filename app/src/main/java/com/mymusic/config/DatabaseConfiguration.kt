package com.mymusic.config

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase

class DatabaseConfiguration {

    private val database: FirebaseFirestore = Firebase.firestore

    init {
        database.firestoreSettings = firestoreSettings {
            isPersistenceEnabled = true
        }
    }

    fun database() = database
}

