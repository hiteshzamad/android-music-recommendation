package com.mymusic.modules.musichistory

import com.google.firebase.firestore.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class MusicHistoryCollection(
    database: FirebaseFirestore,
    uid: String
) {
    private val musicHistoryRef = database.collection("users/$uid/musicHistory")
    private var registration: ListenerRegistration? = null

    fun addListener(listener: EventListener<QuerySnapshot>) {
        registration = musicHistoryRef.orderBy("lastPlayed", Query.Direction.DESCENDING).limit(10)
            .addSnapshotListener(listener)
    }

    fun removeListener() {
        registration?.remove()
    }

    suspend fun update(doc: MusicHistoryDocument) {
        musicHistoryRef.document(doc.id).set(
            hashMapOf(
                "lastPlayed" to doc.lastPlayed
            )
        ).await()
    }
}