package com.mymusic.modules.musichistory

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class MusicHistoryCollection(
    database: FirebaseFirestore,
    uid: String = ""
) {
    private val musicHistoryRef = database.collection("users/$uid/musicHistory")

    suspend fun loadAll(): List<MusicHistoryDocument> {
        val list = mutableListOf<MusicHistoryDocument>()
        val querySnapshot = musicHistoryRef.get().await()
        if (!querySnapshot.isEmpty) {
            try {
                val doc = with(querySnapshot.documents[0]) {
                    MusicHistoryDocument(
                        name = getString("name")!!,
                        artist = getString("artist")!!,
                        count = getLong("count")!!,
                        lastPlayed = getLong("lastPlayed")!!,
                    )
                }
                list.add(doc)
            } catch (e: Exception) {
            }
        }
        return list
    }

    suspend fun upsert(doc: MusicHistoryDocument) {
        val query =
            musicHistoryRef.whereEqualTo("name", doc.name).whereEqualTo("artist", doc.artist)
        val querySnapshot = query.get().await()
        if (querySnapshot.isEmpty) {
            val hashMap = hashMapOf<String, Any>(
                "name" to doc.name,
                "artist" to doc.artist,
                "lastPlayTime" to doc.lastPlayed
            )
            musicHistoryRef.document().set(hashMap).await()
        } else {
            val hashMap = hashMapOf<String, Any>(
                "lastPlayTime" to doc.lastPlayed
            )
            musicHistoryRef.document(querySnapshot.documents[0].id).update(hashMap).await()
        }
    }
}