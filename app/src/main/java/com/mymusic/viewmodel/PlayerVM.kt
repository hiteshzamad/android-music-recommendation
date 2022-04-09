package com.mymusic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mymusic.app.AppContainer
import com.mymusic.model.Music
import com.mymusic.task.Player
import kotlinx.coroutines.launch

class PlayerVM(
    private val player: Player = AppContainer.player
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
            player.addPlayPauseChangeListener(onPlayPauseChangeListener = onPlayPauseChangeListener)
            player.addCurrentMusicChangeListener(currentMusicChangeListener = onCurrentMusicChangeListener)
            player.addCursorPositionChangeListener(onCursorPositionChangeListener = onCursorPositionChangeListener)
            player.addLoopChangeListener(onLoopChangeListener = onLoopChangeListener)
            player.addShuffleChangeListener(onShuffleChangeListener = onShuffleChangeListener)
        }
    }

    override fun onCleared() {
        super.onCleared()
        player.removePlayPauseChangeListener(onPlayPauseChangeListener = onPlayPauseChangeListener)
        player.removeCurrentMusicChangeListener(onCurrentMusicChangeListener = onCurrentMusicChangeListener)
        player.removeCursorPositionChangeListener(onCursorPositionChangeListener = onCursorPositionChangeListener)
        player.removeLoopChangeListener(onLoopChangeListener = onLoopChangeListener)
        player.removeShuffleChangeListener(onShuffleChangeListener = onShuffleChangeListener)
    }

//    fun start(music: Music) {
//        viewModelScope.launch {
//            musicPlayer.start(music = music)
//        }
//    }
//
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

    fun cursorPosition(position: Float) {
        viewModelScope.launch {
            player.cursorPosition(position)
        }
    }

    fun loop(){
        viewModelScope.launch {
            player.loop()
        }
    }

    fun unLoop(){
        viewModelScope.launch {
            player.unLoop()
        }
    }

    fun unShuffle() {
        viewModelScope.launch {
            player.unShuffle()
        }
    }

    fun shuffle(){
        viewModelScope.launch {
            player.shuffle()
        }
    }

    fun previousMusic(){
        viewModelScope.launch {
            player.previous()
        }
    }

    fun nextMusic(){
        viewModelScope.launch {
            player.next()
        }
    }
}