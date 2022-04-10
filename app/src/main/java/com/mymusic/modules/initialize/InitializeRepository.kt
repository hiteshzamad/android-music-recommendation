package com.mymusic.modules.initialize

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mymusic.AppContainer
import com.mymusic.modules.account.AccountCollection

class InitializeRepository(
    private val auth: FirebaseAuth = Firebase.auth,
    private val accountCollection: AccountCollection = AppContainer.accountCollection
) {

    suspend fun getState(): DeviceState {
        auth.currentUser?.let { user ->
            accountCollection.getUser(user.uid)?.let {
                return DeviceState.LOGGED
            }
            return DeviceState.NO_DETAIL
        }
        return DeviceState.NOT_LOGGED
    }
}