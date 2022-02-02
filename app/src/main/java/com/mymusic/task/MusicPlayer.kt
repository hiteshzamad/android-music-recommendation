package com.mymusic.task

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import com.mymusic.model.Music
import java.util.*

class MusicPlayer(private val context: Context) {
    private val mediaPlayer = MediaPlayer()
    private val playPauseChangeListenerList = mutableListOf<(Boolean) -> Unit>()
    private val currentMusicChangeListenerList = mutableListOf<(Music) -> Unit>()
    private val cursorPositionChangeListenerList = mutableListOf<(Float) -> Unit>()
    private val loopListenerChangeListenerList = mutableListOf<(Boolean) -> Unit>()
    private val shuffleListenerChangeListenerList = mutableListOf<(Boolean) -> Unit>()
    private var currentMusic: Music? = null
    private var shuffle = false

    init {
        mediaPlayer.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )
        mediaPlayer.setOnErrorListener { _, _, _ ->
            onPlayPauseChange()
            true
        }
        mediaPlayer.setOnCompletionListener {
            onPlayPauseChange()
        }
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if(mediaPlayer.isPlaying)
                    onCursorPositionChange()
            }
        }, 0, 1000)
    }

    fun start(music: Music) {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.reset()
        mediaPlayer.setDataSource(context, Uri.parse(music.path))
        mediaPlayer.prepare()
        mediaPlayer.start()
        music.duration = (mediaPlayer.duration / 1000.0F)
        onCurrentMusicChange(music = music)
        onPlayPauseChange()
        onCursorPositionChange()
    }

    fun play() = try {
        mediaPlayer.start()
        onPlayPauseChange()
    } catch (e: Exception) {
    }

    fun pause() = try {
        mediaPlayer.pause()
        onPlayPauseChange()
    } catch (e: Exception) {
    }

    fun cursorPosition(float: Float) = try {
        mediaPlayer.seekTo(float.toInt() * 1000)
        onCursorPositionChange()
    } catch (e: Exception) {
    }

    fun loop() = try {
        mediaPlayer.isLooping = true
        onLoopChange()
    } catch (e: Exception) {
    }

    fun unLoop() = try {
        mediaPlayer.isLooping = false
        onLoopChange()
    } catch (e: Exception) {
    }

    fun unShuffle() {
        onShuffleChange(boolean = false)
    }

    fun shuffle() {
        onShuffleChange(boolean = true)
    }

    fun previous() {
    }

    fun next() {
    }

    fun addCurrentMusicChangeListener(currentMusicChangeListener: (Music) -> Unit) {
        currentMusicChangeListenerList.add(currentMusicChangeListener)
        currentMusic?.let { music -> currentMusicChangeListener(music) }
    }

    fun removeCurrentMusicChangeListener(onCurrentMusicChangeListener: (Music) -> Unit) {
        currentMusicChangeListenerList.remove(onCurrentMusicChangeListener)
    }

    fun addPlayPauseChangeListener(onPlayPauseChangeListener: (Boolean) -> Unit) {
        playPauseChangeListenerList.add(onPlayPauseChangeListener)
        onPlayPauseChangeListener(mediaPlayer.isPlaying)
    }

    fun removePlayPauseChangeListener(onPlayPauseChangeListener: (Boolean) -> Unit) {
        playPauseChangeListenerList.remove(onPlayPauseChangeListener)
    }

    fun addCursorPositionChangeListener(onCursorPositionChangeListener: (Float) -> Unit) {
        cursorPositionChangeListenerList.add(onCursorPositionChangeListener)
        onCursorPositionChangeListener(mediaPlayer.currentPosition / 1000F)
    }

    fun removeCursorPositionChangeListener(onCursorPositionChangeListener: (Float) -> Unit) {
        cursorPositionChangeListenerList.remove(onCursorPositionChangeListener)
    }

    fun addLoopChangeListener(onLoopChangeListener: (Boolean) -> Unit) {
        loopListenerChangeListenerList.add(onLoopChangeListener)
        onLoopChangeListener(mediaPlayer.isLooping)
    }

    fun removeLoopChangeListener(onLoopChangeListener: (Boolean) -> Unit) {
        loopListenerChangeListenerList.remove(onLoopChangeListener)
    }

    fun addShuffleChangeListener(onShuffleChangeListener: (Boolean) -> Unit) {
        shuffleListenerChangeListenerList.add(onShuffleChangeListener)
        onShuffleChangeListener(shuffle)
    }

    fun removeShuffleChangeListener(onShuffleChangeListener: (Boolean) -> Unit) {
        shuffleListenerChangeListenerList.remove(onShuffleChangeListener)
    }
    private fun onCurrentMusicChange(music: Music) {
        currentMusic = music
        currentMusicChangeListenerList.forEach { currentMusicChangeListener ->
            currentMusicChangeListener(music)
        }
    }

    private fun onPlayPauseChange() {
        playPauseChangeListenerList.forEach { playPauseChangeListener ->
            playPauseChangeListener(mediaPlayer.isPlaying)
        }
    }

    private fun onCursorPositionChange() {
        cursorPositionChangeListenerList.forEach { cursorPositionChangeListener ->
            cursorPositionChangeListener(mediaPlayer.currentPosition / 1000F)
        }
    }

    private fun onLoopChange() {
        loopListenerChangeListenerList.forEach { loopChangeListener ->
            loopChangeListener(mediaPlayer.isLooping)
        }
    }

    private fun onShuffleChange(boolean: Boolean){
        shuffle = boolean
        shuffleListenerChangeListenerList.forEach { shuffleChangeListener ->
            shuffleChangeListener(boolean)
        }
    }
}