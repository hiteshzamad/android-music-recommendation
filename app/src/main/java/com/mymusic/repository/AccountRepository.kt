package com.mymusic.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mymusic.model.DeviceState

class AccountRepository(
    private val auth: FirebaseAuth = Firebase.auth,
) {
    fun getState(): DeviceState {
        auth.currentUser?.let {
            return DeviceState.LOGGED
        }
        return DeviceState.NOT_LOGGED
    }
}