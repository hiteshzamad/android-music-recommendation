package com.mymusic

import android.content.Context
import com.mymusic.repository.AccountRepository
import com.mymusic.repository.StorageRepository
import com.mymusic.task.MusicPlayer

object AppContainer {
    lateinit var applicationContext: Context

    val musicPlayer by lazy {
        MusicPlayer(applicationContext)
    }

    val storageRepository by lazy {
        StorageRepository(applicationContext)
    }

    val accountRepository by lazy { AccountRepository() }
}