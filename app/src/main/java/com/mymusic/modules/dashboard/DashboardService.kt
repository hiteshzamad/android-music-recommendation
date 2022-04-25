package com.mymusic.modules.dashboard

import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mymusic.AppContainer
import com.mymusic.modules.account.AccountRepository
import com.mymusic.modules.music.Music
import com.mymusic.modules.music.MusicRepository
import com.mymusic.modules.musichistory.MusicHistoryRepository
import com.mymusic.modules.recommendation.RecommendationRepository
import com.mymusic.modules.search.MusicSearch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class DashboardService(
    private val musicHistoryRepository: MusicHistoryRepository = AppContainer.musicHistoryRepository,
    private val recommendationRepository: RecommendationRepository = AppContainer.recommendationRepository,
    private val musicRepository: MusicRepository = AppContainer.musicRepository,
    private val accountRepository: AccountRepository = AppContainer.accountRepository
) {

    val recommendationFlow: Flow<List<Music>> = flow {
        recommendationRepository.readAll().collect { list ->
            val musics = mutableListOf<Music>()
            list.forEach { entity ->
                val music = musicRepository.getMusicById(entity.musicId)
                if (music != null) {
                    musics.add(music)
                }
            }
            emit(musics)
        }
    }

    suspend fun getSearches(name: String) = musicRepository.getMusicSearchList(name)

    suspend fun addHistory(id: String) = musicHistoryRepository.update(id)

    fun attachHistoryListener(listener: EventListener<QuerySnapshot>) =
        musicHistoryRepository.attachListener(listener)

    fun removeHistoryListener() = musicHistoryRepository.removeListener()

    fun startNextRecommendation() {

    }

    suspend fun getMusic(musicSearch: MusicSearch) =
        musicRepository.getMusicByMusicSearch(musicSearch.id, musicSearch.name, musicSearch.artist)

    suspend fun refreshRecommendationList(histories: List<String>) = try {
        val recommendedMusicNames = recommendationRepository.refreshRecommendation(histories)
        val musics = musicRepository.getMusicListByNames(recommendedMusicNames)
        val ids = mutableListOf<String>()
        musics.forEach { music -> ids.add(music.id) }
        recommendationRepository.update(ids)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }

    suspend fun logOut() {
        accountRepository.logOut()
        recommendationRepository.deleteAll()
        Firebase.firestore.clearPersistence()
    }

}