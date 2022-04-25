package com.mymusic.modules.music

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class MusicCollection(database: FirebaseFirestore) {
    private val musicRef = database.collection("songs")
    suspend fun searchByName(search: String): MusicDocument? {
        val querySnapshot = musicRef.whereEqualTo("name", search).get().await()
        if (!querySnapshot.isEmpty) {
            val id = querySnapshot.documents[0].id
            val name = querySnapshot.documents[0]["name"] as String
            val artist = querySnapshot.documents[0]["artist"] as String
            val genre = querySnapshot.documents[0]["genre"] as String
            val movie = querySnapshot.documents[0]["movie"] as String
            return MusicDocument(id, name, artist, genre, movie)
        }
        return null
    }

    suspend fun searchById(id: String): MusicDocument? {
        val documentSnapshot = musicRef.document(id).get().await()
        if (documentSnapshot.exists()) {
            val name = documentSnapshot["name"] as String
            val artist = documentSnapshot["artist"] as String
            val genre = documentSnapshot["genre"] as String
            val movie = documentSnapshot["movie"] as String
            return MusicDocument(id, name, artist, genre, movie)
        }
        return null
    }

    suspend fun queryByName(search: String): List<MusicDocument> {
        val list = mutableListOf<MusicDocument>()
        val querySnapshot = musicRef.whereEqualTo("name", search).get().await()
        if (!querySnapshot.isEmpty) {
            val id = querySnapshot.documents[0].id
            val name = querySnapshot.documents[0]["name"] as String
            val artist = querySnapshot.documents[0]["artist"] as String
            val genre = querySnapshot.documents[0]["genre"] as String
            val movie = querySnapshot.documents[0]["movie"] as String
            list.add(MusicDocument(id, name, artist, genre, movie))
        }
        return list
    }
}