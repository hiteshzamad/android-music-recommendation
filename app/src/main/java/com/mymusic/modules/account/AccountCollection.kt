package com.mymusic.modules.account

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AccountCollection(
    private val database: FirebaseFirestore
) {
    suspend fun getUser(uid: String): Account? {
        val dataSnapshot = database.collection("users").document(uid).get().await()
        if (dataSnapshot.exists()) {
            val name = dataSnapshot.data?.get("name") as String
            val gender = dataSnapshot.data?.get("gender") as String
            val dob = dataSnapshot.data?.get("dob") as String
            return Account(name, gender, dob)
        }
        return null
    }

    suspend fun setUser(uid: String, account: Account) {
        val hashMap = hashMapOf(
            "name" to account.name,
            "gender" to account.gender,
            "dob" to account.dob
        )
        database.collection("users").document(uid).set(hashMap).await()
    }
}