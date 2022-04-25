package com.mymusic

import android.content.Context
import com.mymusic.config.LocalDatabase
import com.mymusic.config.RemoteDatabase
import com.mymusic.config.RetrofitConfiguration
import com.mymusic.modules.account.AccountRepository
import com.mymusic.modules.download.DownloadService
import com.mymusic.modules.initialize.InitializeRepository
import com.mymusic.modules.localmusic.LocalMusicRepository
import com.mymusic.modules.music.MusicApi
import com.mymusic.modules.music.MusicRepository
import com.mymusic.modules.musichistory.MusicHistoryRepository
import com.mymusic.modules.musicplayer.MusicPlayerService
import com.mymusic.modules.recommendation.RecommendationApi
import com.mymusic.modules.recommendation.RecommendationRepository

object AppContainer {
    lateinit var applicationContext: Context
    private val retrofitConfiguration by lazy { RetrofitConfiguration() }
    private val remoteDatabase by lazy { RemoteDatabase() }
    private val localDatabase by lazy { LocalDatabase.getDatabase(applicationContext) }

    val musicPlayerService by lazy { MusicPlayerService(applicationContext) }
    val downloadService by lazy { DownloadService(applicationContext) }

    val recommendationApi: RecommendationApi by lazy { retrofitConfiguration.recommendationApiService() }
    val musicApi: MusicApi by lazy { retrofitConfiguration.songApiService() }

    val accountCollection by lazy { remoteDatabase.accountCollection() }
    val musicHistoryCollection by lazy { remoteDatabase.musicHistoryCollection() }
    val musicCollection by lazy{ remoteDatabase.musicCollection()}

    val recommendationDao by lazy { localDatabase.recommendationDao() }

    val musicRepository by lazy { MusicRepository() }
    val accountRepository by lazy { AccountRepository() }
    val localMusicRepository by lazy { LocalMusicRepository(applicationContext) }
    val recommendationRepository by lazy { RecommendationRepository() }
    val initializeRepository by lazy { InitializeRepository() }
    val musicHistoryRepository by lazy { MusicHistoryRepository() }

}