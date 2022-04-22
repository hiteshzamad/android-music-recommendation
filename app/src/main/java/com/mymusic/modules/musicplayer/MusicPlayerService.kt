package com.mymusic.modules.musicplayer

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import com.mymusic.modules.music.Music
import com.mymusic.util.DataListener
import java.util.*

class MusicPlayerService(private val context: Context) {
    private val mediaPlayer = MediaPlayer()
    val stateDataListener = DataListener(MusicPlayerState.PAUSE)
    val musicDataListener = DataListener<Music>(null)
    val cursorDataListener = DataListener(0.0F)
    val loopDataListener = DataListener(false)
    val durationDataListener = DataListener(0.0F)

    private var prepared = false
    private val timerTask: TimerTask = object : TimerTask() {
        override fun run() {
            try {
                if (mediaPlayer.isPlaying) {
                    cursorDataListener.set(mediaPlayer.currentPosition / 1000F)
                }
            } catch (e: Exception) {

            }
        }
    }

    init {
        mediaPlayer.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )
        mediaPlayer.setOnErrorListener { _,_,_ ->
            stateDataListener.set(MusicPlayerState.PAUSE)
            true
        }
        mediaPlayer.setOnCompletionListener {
            stateDataListener.set(MusicPlayerState.COMPLETED)
        }
        mediaPlayer.setOnPreparedListener {
            prepared = true
            stateDataListener.set(MusicPlayerState.PREPARED)
            mediaPlayer.start()
            durationDataListener.set(mediaPlayer.duration / 1000F)
            stateDataListener.set(MusicPlayerState.PLAY)
        }
        Timer().scheduleAtFixedRate(timerTask, 0, 1000)
    }

    fun start(music1: Music) {
        prepared = false
        stateDataListener.set(MusicPlayerState.PAUSE)
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.reset()
        musicDataListener.set(music1)
        stateDataListener.set(MusicPlayerState.LOADING)
        mediaPlayer.setDataSource(context, Uri.parse(music1.path))
        durationDataListener.set(0f)
        cursorDataListener.set(0f)
        mediaPlayer.prepareAsync()
    }

    fun play() {
        try {
            if (prepared) {
                mediaPlayer.start()
                if (mediaPlayer.isPlaying) {
                    stateDataListener.set(MusicPlayerState.PLAY)
                }
            } else {
                musicDataListener.t?.let { start(it) }
            }
        } catch (e: Exception) {
        }
    }

    fun pause() {
        try {
            mediaPlayer.pause()
            if (!mediaPlayer.isPlaying) {
                stateDataListener.set(MusicPlayerState.PAUSE)
            }
        } catch (e: Exception) {
        }
    }

    fun cursor(float: Float) = try {
        mediaPlayer.seekTo(float.toInt() * 1000)
        cursorDataListener.set(mediaPlayer.currentPosition / 1000F)
    } catch (e: Exception) {
    }

    fun loop() = try {
        mediaPlayer.isLooping = true
        loopDataListener.set(mediaPlayer.isLooping)
    } catch (e: Exception) {
    }

    fun unLoop() = try {
        mediaPlayer.isLooping = false
        loopDataListener.set(mediaPlayer.isLooping)
    } catch (e: Exception) {
    }

    fun clear() {
        timerTask.cancel()
        mediaPlayer.release()
        stateDataListener.removeAll()
        musicDataListener.removeAll()
        cursorDataListener.removeAll()
        durationDataListener.removeAll()
        loopDataListener.removeAll()
    }
}