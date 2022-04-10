package com.mymusic.modules.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mymusic.AppContainer
import com.mymusic.model.Music
import com.mymusic.modules.musichistory.MusicHistoryRepository
import com.mymusic.modules.musicplayer.MusicPlayerService
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val musicPlayerService: MusicPlayerService = AppContainer.musicPlayerService,
    private val musicHistoryRepository: MusicHistoryRepository = AppContainer.musicHistoryRepository
) : ViewModel() {

    val currentMusic = MutableLiveData<Music>()
    val musicRunning = MutableLiveData(false)

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
    }

    override fun onCleared() {
        musicPlayerService.removePlayPauseChangeListener(onPlayPauseChangeListener = onPlayPauseChangeListener)
        musicPlayerService.removeCurrentMusicChangeListener(onCurrentMusicChangeListener = onCurrentMusicChangeListener)
        super.onCleared()
    }

    fun start(music: Music) {
        viewModelScope.launch {
            try {
                musicHistoryRepository.add(music.name)
                musicPlayerService.start(music = music)
            }catch (e: Exception){

            }
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
}