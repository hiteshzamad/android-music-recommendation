package com.mymusic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mymusic.AppContainer
import com.mymusic.model.Music
import com.mymusic.task.MusicPlayer
import kotlinx.coroutines.launch

class MusicPlayerViewModel(
    private val musicPlayer: MusicPlayer = AppContainer.musicPlayer
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
            musicPlayer.addPlayPauseChangeListener(onPlayPauseChangeListener = onPlayPauseChangeListener)
            musicPlayer.addCurrentMusicChangeListener(currentMusicChangeListener = onCurrentMusicChangeListener)
            musicPlayer.addCursorPositionChangeListener(onCursorPositionChangeListener = onCursorPositionChangeListener)
            musicPlayer.addLoopChangeListener(onLoopChangeListener = onLoopChangeListener)
            musicPlayer.addShuffleChangeListener(onShuffleChangeListener = onShuffleChangeListener)
        }
    }

    override fun onCleared() {
        super.onCleared()
        musicPlayer.removePlayPauseChangeListener(onPlayPauseChangeListener = onPlayPauseChangeListener)
        musicPlayer.removeCurrentMusicChangeListener(onCurrentMusicChangeListener = onCurrentMusicChangeListener)
        musicPlayer.removeCursorPositionChangeListener(onCursorPositionChangeListener = onCursorPositionChangeListener)
        musicPlayer.removeLoopChangeListener(onLoopChangeListener = onLoopChangeListener)
        musicPlayer.removeShuffleChangeListener(onShuffleChangeListener = onShuffleChangeListener)
    }

//    fun start(music: Music) {
//        viewModelScope.launch {
//            musicPlayer.start(music = music)
//        }
//    }
//
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

    fun cursorPosition(position: Float) {
        viewModelScope.launch {
            musicPlayer.cursorPosition(position)
        }
    }

    fun loop(){
        viewModelScope.launch {
            musicPlayer.loop()
        }
    }

    fun unLoop(){
        viewModelScope.launch {
            musicPlayer.unLoop()
        }
    }

    fun unShuffle() {
        viewModelScope.launch {
            musicPlayer.unShuffle()
        }
    }

    fun shuffle(){
        viewModelScope.launch {
            musicPlayer.shuffle()
        }
    }

    fun previousMusic(){
        viewModelScope.launch {
            musicPlayer.previous()
        }
    }

    fun nextMusic(){
        viewModelScope.launch {
            musicPlayer.next()
        }
    }
}