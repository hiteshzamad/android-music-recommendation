package com.mymusic.modules.musichistory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mymusic.AppContainer
import com.mymusic.model.Music
import com.mymusic.modules.song.SongRepository
import com.mymusic.util.Resource
import kotlinx.coroutines.launch

class MusicHistoryViewModel(
    private val musicHistoryRepository: MusicHistoryRepository = AppContainer.musicHistoryRepository,
    private val songRepository: SongRepository = AppContainer.songRepository
) : ViewModel() {

    val historyList = MutableLiveData<Resource<List<Music>>>()

    init {
        history()
    }

    private fun history() {
        viewModelScope.launch {
            try {
                val tempHistoryList = musicHistoryRepository.getAll()
                val musicList = songRepository.getMusicList(tempHistoryList)
                historyList.value = Resource(musicList)
            } catch (e: Exception) {
                historyList.value = Resource(null)
                return@launch
            }
        }
    }

}