package com.mymusic.modules.account

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mymusic.AppContainer
import com.mymusic.datasource.RemoteDatabase
import com.mymusic.modules.initialize.DeviceState
import com.mymusic.model.User
import kotlinx.coroutines.tasks.await

class AccountRepository(
    private val auth: FirebaseAuth = Firebase.auth,
    private val remoteDatabase: RemoteDatabase = AppContainer.remoteDatabase
) {

    suspend fun addDetail(name: String, gender: String, dob: String) {
        remoteDatabase.setUser(auth.currentUser!!.uid, User(name, gender, dob))
    }

    suspend fun getUser(): User {
        val user = remoteDatabase.getUser(auth.currentUser!!.uid)!!
        user.email = auth.currentUser!!.email
        return user
    }

    suspend fun logIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
    }

    suspend fun forgotPassword(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

}