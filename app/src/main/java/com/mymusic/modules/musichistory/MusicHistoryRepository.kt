package com.mymusic.modules.musichistory

import androidx.annotation.WorkerThread
import com.mymusic.AppContainer
import com.mymusic.Time

class MusicHistoryRepository(
    private val musicHistoryDao: MusicHistoryDao = AppContainer.musicHistoryDao,
    private val musicHistoryCollection: MusicHistoryCollection = AppContainer.musicHistoryCollection
) {

    fun readRecentTen() = musicHistoryDao.loadRecent10()

    @WorkerThread
    suspend fun upsert(id: Long) {
        val musicHistoryEntity = musicHistoryDao.loadById(id)
        if (musicHistoryEntity == null) {
            musicHistoryDao.insert(MusicHistoryEntity(id, 1, Time.now()))
        } else {
            musicHistoryEntity.count++
            musicHistoryEntity.lastPlayed = Time.now()
            musicHistoryDao.update(musicHistoryEntity)
        }
    }

    suspend fun deleteAll() {
        musicHistoryDao.deleteAll()
    }
}