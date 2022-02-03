package com.mymusic.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.mymusic.model.User
import kotlinx.coroutines.tasks.await

class RemoteDatabase {

    private val database: FirebaseFirestore = Firebase.firestore

    init {
        database.firestoreSettings = firestoreSettings {
            isPersistenceEnabled = true
        }
    }

    suspend fun getUser(uid: String): User? {
        val dataSnapshot = database.collection("users").document(uid).get().await()
        if (dataSnapshot.exists()) {
            val name = dataSnapshot.data?.get("name") as String
            val gender = dataSnapshot.data?.get("gender") as String
            val dob = dataSnapshot.data?.get("dob") as String
            return User(name, gender, dob)
        }
        return null
    }

    suspend fun setUser(uid: String, user: User) {
        val hashMap = hashMapOf(
            "name" to user.name,
            "gender" to user.gender,
            "dob" to user.dob
        )
        database.collection("users").document(uid).set(hashMap).await()
    }

    suspend fun getListenHistory(uid: String): List<String> {
        val list = mutableListOf<String>()
        val dataSnapshot = database.collection("users").document(uid).get().await()
        if (dataSnapshot.exists()) {
            val listenHistory = dataSnapshot.data?.get("listenHistory")
            listenHistory?.let { listenHistory1 ->
                val listenHistoryList = listenHistory1 as ArrayList<*>
                listenHistoryList.forEach { item ->
                    item?.let { item1 ->
                        val map = item1 as HashMap<*, *>
                        map["name"]?.let { name ->
                            list.add(name as String)
                        }
                    }
                }
            }
        }
        return list
    }
}