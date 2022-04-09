package com.mymusic

import android.content.Context
import com.mymusic.datasource.RemoteDatabase
import com.mymusic.modules.initialize.InitializeRepository
import com.mymusic.modules.localmusic.LocalMusicRepository
import com.mymusic.modules.musichistory.MusicHistoryRepository
import com.mymusic.modules.musicplayer.MusicPlayerService
import com.mymusic.modules.account.AccountRepository
import com.mymusic.modules.recommendation.RecommendationRepository
import com.mymusic.modules.recommendation.RecommendationApiService
import com.mymusic.retrofit.RetrofitBuilder

object AppContainer {
    private val retrofit by lazy { RetrofitBuilder.recommendationBuild() }
    lateinit var applicationContext: Context

    val musicPlayerService by lazy { MusicPlayerService(applicationContext) }
    val storageRepository by lazy { LocalMusicRepository(applicationContext) }
    val remoteDatabase by lazy { RemoteDatabase() }
    val accountRepository by lazy { AccountRepository() }
    val restRepository by lazy { RecommendationRepository() }
    val initializeRepository by lazy { InitializeRepository() }
    val musicHistoryRepository by lazy { MusicHistoryRepository() }
    val apiService: RecommendationApiService by lazy { retrofit.create(RecommendationApiService::class.java) }
}