package com.mymusic.modules.musichistory

import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.mymusic.AppContainer
import com.mymusic.Time

class MusicHistoryRepository(
    private val musicHistoryCollection: MusicHistoryCollection = AppContainer.musicHistoryCollection
) {

    fun attachListener(listener: EventListener<QuerySnapshot>) =
        musicHistoryCollection.addListener(listener)

    fun removeListener() = musicHistoryCollection.removeListener()

    suspend fun update(id: String) {
        musicHistoryCollection.update(MusicHistoryDocument(id, Time.now()))
    }
}