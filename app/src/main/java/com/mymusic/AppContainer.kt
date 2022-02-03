package com.mymusic

import android.content.Context
import com.mymusic.datasource.RemoteDatabase
import com.mymusic.repository.AccountRepository
import com.mymusic.repository.LocalStorageRepository
import com.mymusic.repository.RestRepository
import com.mymusic.rest.ApiService
import com.mymusic.rest.RetrofitBuilder
import com.mymusic.task.Player

object AppContainer {
    private val retrofit by lazy { RetrofitBuilder.build() }
    lateinit var applicationContext: Context

    val player by lazy { Player(applicationContext) }
    val storageRepository by lazy { LocalStorageRepository(applicationContext) }
    val remoteDatabase by lazy { RemoteDatabase() }
    val accountRepository by lazy { AccountRepository() }
    val restRepository by lazy { RestRepository() }
    val apiService: ApiService by lazy { retrofit.create(ApiService::class.java) }
}