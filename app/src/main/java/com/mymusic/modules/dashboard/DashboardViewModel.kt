package com.mymusic.modules.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mymusic.AppContainer
import com.mymusic.model.Music
import com.mymusic.model.Resource
import com.mymusic.model.Task
import com.mymusic.modules.musichistory.MusicHistoryRepository
import com.mymusic.modules.recommendation.RecommendationRepository
import com.mymusic.modules.musicplayer.MusicPlayerService
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val musicPlayerService: MusicPlayerService = AppContainer.musicPlayerService,
    private val musicHistoryRepository: MusicHistoryRepository = AppContainer.musicHistoryRepository,
    private val restRepository: RecommendationRepository = AppContainer.restRepository
) : ViewModel() {

    val currentMusic = MutableLiveData<Music>()
    val musicRunning = MutableLiveData(false)
    val historyList = MutableLiveData<Resource<List<Music>>>()
    val recommendationList = MutableLiveData<Task<List<Music>>>()

    private val onPlayPauseChangeListener = { boolean: Boolean ->
        musicRunning.postValue(boolean)
    }

    private val onCurrentMusicChangeListener = { music1: Music ->
        this.currentMusic.postValue(music1)
    }

    init {
        viewModelScope.launch {
            musicPlayerService.addPlayPauseChangeListener(onPlayPauseChangeListener = onPlayPauseChangeListener)
            musicPlayerService.addCurrentMusicChangeListener(currentMusicChangeListener = onCurrentMusicChangeListener)
        }
        recommend()
    }

    override fun onCleared() {
        super.onCleared()
        musicPlayerService.removePlayPauseChangeListener(onPlayPauseChangeListener = onPlayPauseChangeListener)
        musicPlayerService.removeCurrentMusicChangeListener(onCurrentMusicChangeListener = onCurrentMusicChangeListener)
    }

    fun start(music: Music) {
        viewModelScope.launch {
            musicPlayerService.start(music = music)
        }
    }

    fun play() {
        viewModelScope.launch {
            musicPlayerService.play()
        }
    }

    fun pause() {
        viewModelScope.launch {
            musicPlayerService.pause()
        }
    }

    fun history(){
        viewModelScope.launch {
            try {
                val tempHistoryList = musicHistoryRepository.getListenHistory()
                historyList.value = Resource(toMusicList(tempHistoryList))
            } catch (e: Exception) {

            }
        }
    }

    fun recommend() {
        viewModelScope.launch {
            try {
                val tempHistoryList = musicHistoryRepository.getListenHistory()
                historyList.value = Resource(toMusicList(tempHistoryList))
                val recommendList = restRepository.getRecommends(tempHistoryList)
                recommendationList.value = Task.Success(toMusicList(recommendList))
            } catch (e: Exception) {
                var message = "Error"
                e.message?.let { message = it }
                recommendationList.value = Task.Failed(message = message)
                return@launch
            }
        }
    }

    private fun toMusicList(list: List<String>) : List<Music>{
        val listM = mutableListOf<Music>()
        list.forEach {
            listM.add(Music(path = "", name = it))
        }
        return listM
    }
}