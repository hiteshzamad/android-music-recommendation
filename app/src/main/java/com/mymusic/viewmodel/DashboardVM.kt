package com.mymusic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mymusic.AppContainer
import com.mymusic.model.Music
import com.mymusic.model.Resource
import com.mymusic.model.Task
import com.mymusic.repository.AccountRepository
import com.mymusic.repository.RestRepository
import com.mymusic.task.Player
import kotlinx.coroutines.launch

class DashboardVM(
    private val player: Player = AppContainer.player,
    private val accountRepository: AccountRepository = AppContainer.accountRepository,
    private val restRepository: RestRepository = AppContainer.restRepository
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
            player.addPlayPauseChangeListener(onPlayPauseChangeListener = onPlayPauseChangeListener)
            player.addCurrentMusicChangeListener(currentMusicChangeListener = onCurrentMusicChangeListener)
        }
        recommend()
    }

    override fun onCleared() {
        super.onCleared()
        player.removePlayPauseChangeListener(onPlayPauseChangeListener = onPlayPauseChangeListener)
        player.removeCurrentMusicChangeListener(onCurrentMusicChangeListener = onCurrentMusicChangeListener)
    }

    fun start(music: Music) {
        viewModelScope.launch {
            player.start(music = music)
        }
    }

    fun play() {
        viewModelScope.launch {
            player.play()
        }
    }

    fun pause() {
        viewModelScope.launch {
            player.pause()
        }
    }

    fun history(){
        viewModelScope.launch {
            try {
                val tempHistoryList = accountRepository.getListenHistory()
                historyList.value = Resource(toMusicList(tempHistoryList))
            } catch (e: Exception) {

            }
        }
    }

    fun recommend() {
        viewModelScope.launch {
            try {
                val tempHistoryList = accountRepository.getListenHistory()
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