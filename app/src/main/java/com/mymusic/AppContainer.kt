package com.mymusic

import android.content.Context
import com.mymusic.config.DatabaseConfiguration
import com.mymusic.config.RetrofitConfiguration
import com.mymusic.modules.account.AccountCollection
import com.mymusic.modules.account.AccountRepository
import com.mymusic.modules.initialize.InitializeRepository
import com.mymusic.modules.localmusic.LocalMusicRepository
import com.mymusic.modules.musichistory.MusicHistoryCollection
import com.mymusic.modules.musichistory.MusicHistoryRepository
import com.mymusic.modules.musicplayer.MusicPlayerService
import com.mymusic.modules.recommendation.RecommendationApiService
import com.mymusic.modules.recommendation.RecommendationRepository
import com.mymusic.modules.song.SongApiService
import com.mymusic.modules.song.SongRepository

object AppContainer {
    lateinit var applicationContext: Context

    private val retrofitConfiguration by lazy { RetrofitConfiguration() }
    private val databaseConfiguration by lazy { DatabaseConfiguration() }

    val musicPlayerService by lazy { MusicPlayerService(applicationContext) }

    val recommendationApiService: RecommendationApiService by lazy { retrofitConfiguration.recommendationApiService() }
    val songApiService: SongApiService by lazy { retrofitConfiguration.songApiService() }

    val accountCollection by lazy { AccountCollection(databaseConfiguration.database()) }
    val musicHistoryCollection by lazy { MusicHistoryCollection(databaseConfiguration.database()) }

    val songRepository by lazy { SongRepository() }
    val accountRepository by lazy { AccountRepository() }
    val localMusicRepository by lazy { LocalMusicRepository(applicationContext) }
    val recommendationRepository by lazy { RecommendationRepository() }
    val initializeRepository by lazy { InitializeRepository() }
    val musicHistoryRepository by lazy { MusicHistoryRepository() }
}