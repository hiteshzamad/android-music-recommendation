package com.mymusic.modules.musicplayer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mymusic.AppContainer
import com.mymusic.modules.download.DownloadService
import com.mymusic.modules.download.DownloadState
import com.mymusic.modules.music.Music
import kotlinx.coroutines.launch

class MusicPlayerViewModel(
    private val musicPlayerService: MusicPlayerService = AppContainer.musicPlayerService,
    private val downloadService: DownloadService = AppContainer.downloadService
) : ViewModel() {

    val downloadState = MutableLiveData(DownloadState.NOT_DOWNLOADED)
    val music = MutableLiveData<Music>()
    val playerState = MutableLiveData(MusicPlayerState.PAUSE)
    val cursor = MutableLiveData(0F)
    val loop = MutableLiveData(false)
    val shuffle = MutableLiveData(false)
    val duration = MutableLiveData(0F)

    private val musicListener = { music: Music ->
        this.music.postValue(music)
    }

    private val cursorListener = { float: Float ->
        cursor.postValue(float)
    }

    private val loopListener = { boolean: Boolean ->
        loop.postValue(boolean)
    }

    private val durationListener = { float: Float ->
        duration.postValue(float)
    }

    private val playerStateListener = { musicPlayerState: MusicPlayerState ->
        playerState.postValue(musicPlayerState)
        if (musicPlayerState == MusicPlayerState.LOADING) {
            when {
                music.value?.localPath != null -> {
                    downloadState.value = DownloadState.DOWNLOADED
                }
                else -> {
                    downloadState.value = DownloadState.NOT_DOWNLOADED
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            musicPlayerService.stateDataListener.addAndListen(playerStateListener)
            musicPlayerService.cursorDataListener.addAndListen(cursorListener)
            musicPlayerService.loopDataListener.addAndListen(loopListener)
            musicPlayerService.musicDataListener.addAndListen(musicListener)
            musicPlayerService.durationDataListener.addAndListen(durationListener)
        }
    }

    override fun onCleared() {
        super.onCleared()
        musicPlayerService.cursorDataListener.remove(cursorListener)
        musicPlayerService.loopDataListener.remove(loopListener)
        musicPlayerService.musicDataListener.remove(musicListener)
        musicPlayerService.stateDataListener.remove(playerStateListener)
        musicPlayerService.durationDataListener.remove(durationListener)
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

    fun cursor(position: Float) {
        viewModelScope.launch {
            musicPlayerService.cursor(position)
        }
    }

    fun loop() {
        viewModelScope.launch {
            musicPlayerService.loop()
        }
    }

    fun unLoop() {
        viewModelScope.launch {
            musicPlayerService.unLoop()
        }
    }

    fun unShuffle() {
        viewModelScope.launch {
        }
    }

    fun shuffle() {
        viewModelScope.launch {
        }
    }

    fun previousMusic() {
        viewModelScope.launch {
        }
    }

    fun nextMusic() {
        viewModelScope.launch {
        }
    }

    fun downloadMusic(path: String, name: String) {
        viewModelScope.launch {
            downloadService.startDownload(path, name)
        }
    }
}