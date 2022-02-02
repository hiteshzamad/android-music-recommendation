package com.mymusic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mymusic.AppContainer
import com.mymusic.model.Music
import com.mymusic.task.MusicPlayer
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val musicPlayer: MusicPlayer = AppContainer.musicPlayer
) : ViewModel() {

    var currentMusic = MutableLiveData<Music>()
    var play = MutableLiveData(false)

    private val onPlayPauseChangeListener = { boolean: Boolean ->
        play.postValue(boolean)
    }

    private val onCurrentMusicChangeListener = { music: Music ->
        currentMusic.postValue(music)
    }

    init {
        viewModelScope.launch {
            musicPlayer.addPlayPauseChangeListener(onPlayPauseChangeListener = onPlayPauseChangeListener)
            musicPlayer.addCurrentMusicChangeListener(currentMusicChangeListener = onCurrentMusicChangeListener)
        }
    }

    override fun onCleared() {
        super.onCleared()
        musicPlayer.removePlayPauseChangeListener(onPlayPauseChangeListener = onPlayPauseChangeListener)
        musicPlayer.removeCurrentMusicChangeListener(onCurrentMusicChangeListener = onCurrentMusicChangeListener)
    }

    fun start(music: Music) {
        viewModelScope.launch {
            musicPlayer.start(music = music)
        }
    }

    fun play() {
        viewModelScope.launch {
            musicPlayer.play()
        }
    }

    fun pause() {
        viewModelScope.launch {
            musicPlayer.pause()
        }
    }
}