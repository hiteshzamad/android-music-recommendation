package com.mymusic.modules.recommendation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mymusic.AppContainer
import com.mymusic.model.Music
import com.mymusic.modules.musichistory.MusicHistoryRepository
import com.mymusic.modules.song.SongRepository
import com.mymusic.util.Task
import kotlinx.coroutines.launch

class RecommendationViewModel(
    private val musicHistoryRepository: MusicHistoryRepository = AppContainer.musicHistoryRepository,
    private val recommendationRepository: RecommendationRepository = AppContainer.recommendationRepository,
    private val songRepository: SongRepository = AppContainer.songRepository
) : ViewModel() {

    val recommendationList = MutableLiveData<Task<List<Music>>>()

    init {
        recommend()
    }

    private fun recommend() {
        viewModelScope.launch {
            try {
                val historyList = musicHistoryRepository.getAll()
                val recommendList = recommendationRepository.getRecommends(historyList)
                val musicList = songRepository.getMusicList(recommendList)
                recommendationList.value = Task.Success(musicList)
            } catch (e: Exception) {
                var message = "Error"
                e.message?.let { message = it }
                recommendationList.value = Task.Failed(message = message)
                return@launch
            }
        }
    }

}