package com.mymusic.modules.dashboard

import com.mymusic.AppContainer
import com.mymusic.modules.account.AccountRepository
import com.mymusic.modules.music.Music
import com.mymusic.modules.music.MusicRepository
import com.mymusic.modules.music.toMusic
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
    private val histories = mutableListOf<String>()

    val historyFlow: Flow<List<Music>> = flow {
        musicHistoryRepository.readRecentTen().collect { list ->
            val musics = mutableListOf<Music>()
            val names = mutableListOf<String>()
            list.forEach { entity ->
                musicRepository.getMusic(entity.musicId)?.let { entity1 ->
                    musics.add(entity1.toMusic())
                    names.add(entity1.name)
                }
            }
            emit(musics)
            histories.clear()
            histories.addAll(names)
        }
    }

    val recommendationFlow: Flow<List<Music>> = flow {
        recommendationRepository.readAll().collect { list ->
            val ids = mutableListOf<Long>()
            list.forEach { entity -> ids.add(entity.musicId) }
            val musicsEntities = musicRepository.getMusics(ids)
            val musics = mutableListOf<Music>()
            musicsEntities.forEach { musicEntity ->
                musics.add(musicEntity.toMusic())
            }
            emit(musics)
        }
    }

    suspend fun getSearches(name: String) = musicRepository.getSearchList(name)

    suspend fun addHistory(id: Long) = musicHistoryRepository.upsert(id)

    suspend fun startNextRecommendation() {

    }

    suspend fun getMusic(musicSearch: MusicSearch) = musicRepository.getMusic(musicSearch)

    suspend fun refreshRecommendationList() = try {
        val recommendationStrings = recommendationRepository.refreshRecommendation(histories)
        val musics = musicRepository.getBestMatchMusicList(recommendationStrings)
        val ids = mutableListOf<Long>()
        musics.forEach { music -> ids.add(music.id) }
        recommendationRepository.update(ids)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }

    suspend fun logOut() {
        accountRepository.logOut()
        musicHistoryRepository.deleteAll()
        musicRepository.deleteAll()
        recommendationRepository.deleteAll()
    }

}