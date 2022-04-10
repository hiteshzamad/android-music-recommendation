package com.mymusic.modules.musichistory

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class MusicHistoryCollection(
    private val database: FirebaseFirestore
) {
    suspend fun getAll(uid: String): List<String> {
        val list = mutableListOf<String>()
        val querySnapshot = database.collection("users/$uid/musicHistory").get().await()
        querySnapshot.forEach { queryDatabaseSnapshot ->
            list.add(queryDatabaseSnapshot.id)
        }
        return list
    }

    suspend fun add(uid: String, name: String) {
        val musicReference = database.collection("users/$uid/musicHistory").document(name)
        val documentSnapshot = musicReference.get().await()
        if (documentSnapshot.exists()) {
            musicReference.update("count", FieldValue.increment(1))
            musicReference.update("lastPlayTime", FieldValue.serverTimestamp())
        } else {
            val hashMap = hashMapOf(
                "count" to 1,
                "lastPlayTime" to FieldValue.serverTimestamp()
            )
            musicReference.set(hashMap).await()
        }
    }
}