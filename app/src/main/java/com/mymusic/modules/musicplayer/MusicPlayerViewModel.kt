package com.mymusic.modules.musicplayer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mymusic.AppContainer
import com.mymusic.model.Music
import kotlinx.coroutines.launch

class MusicPlayerViewModel(
    private val musicPlayerService: MusicPlayerService = AppContainer.musicPlayerService
) : ViewModel() {

    var currentMusic = MutableLiveData<Music>()
    var play = MutableLiveData(false)
    var cursorPosition = MutableLiveData(0F)
    var loop = MutableLiveData(false)
    var shuffle = MutableLiveData(false)

    private val onShuffleChangeListener = { boolean: Boolean ->
        shuffle.postValue(boolean)
    }

    private val onPlayPauseChangeListener = { boolean: Boolean ->
        play.postValue(boolean)
    }

    private val onCurrentMusicChangeListener = { music: Music ->
        currentMusic.postValue(music)
    }

    private val onCursorPositionChangeListener = { float: Float ->
        cursorPosition.postValue(float)
    }

    private val onLoopChangeListener = { boolean : Boolean ->
        loop.postValue(boolean)
    }

    init {
        viewModelScope.launch {
            musicPlayerService.addPlayPauseChangeListener(onPlayPauseChangeListener = onPlayPauseChangeListener)
            musicPlayerService.addCurrentMusicChangeListener(currentMusicChangeListener = onCurrentMusicChangeListener)
            musicPlayerService.addCursorPositionChangeListener(onCursorPositionChangeListener = onCursorPositionChangeListener)
            musicPlayerService.addLoopChangeListener(onLoopChangeListener = onLoopChangeListener)
            musicPlayerService.addShuffleChangeListener(onShuffleChangeListener = onShuffleChangeListener)
        }
    }

    override fun onCleared() {
        super.onCleared()
        musicPlayerService.removePlayPauseChangeListener(onPlayPauseChangeListener = onPlayPauseChangeListener)
        musicPlayerService.removeCurrentMusicChangeListener(onCurrentMusicChangeListener = onCurrentMusicChangeListener)
        musicPlayerService.removeCursorPositionChangeListener(onCursorPositionChangeListener = onCursorPositionChangeListener)
        musicPlayerService.removeLoopChangeListener(onLoopChangeListener = onLoopChangeListener)
        musicPlayerService.removeShuffleChangeListener(onShuffleChangeListener = onShuffleChangeListener)
    }

//    fun start(music: Music) {
//        viewModelScope.launch {
//            musicPlayer.start(music = music)
//        }
//    }
//
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

    fun cursorPosition(position: Float) {
        viewModelScope.launch {
            musicPlayerService.cursorPosition(position)
        }
    }

    fun loop(){
        viewModelScope.launch {
            musicPlayerService.loop()
        }
    }

    fun unLoop(){
        viewModelScope.launch {
            musicPlayerService.unLoop()
        }
    }

    fun unShuffle() {
        viewModelScope.launch {
            musicPlayerService.unShuffle()
        }
    }

    fun shuffle(){
        viewModelScope.launch {
            musicPlayerService.shuffle()
        }
    }

    fun previousMusic(){
        viewModelScope.launch {
            musicPlayerService.previous()
        }
    }

    fun nextMusic(){
        viewModelScope.launch {
            musicPlayerService.next()
        }
    }
}