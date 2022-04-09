package com.mymusic.modules.localmusic

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.mymusic.model.Music

class LocalMusicRepository(private val context: Context) {

    fun getMusicList(): List<Music> {
        val musicList = mutableListOf<Music>()
        context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.Audio.AlbumColumns.ALBUM_ID,
                MediaStore.Audio.AudioColumns.DISPLAY_NAME,
                MediaStore.Audio.ArtistColumns.ARTIST,
                MediaStore.Audio.AudioColumns._ID
            ),
            null,
            null,
            null
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                val music = Music(
                    path = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        cursor.getInt(3).toLong()
                    ).toString()
                )
                music.name = cursor.getString(1)
                music.artist = cursor.getString(2)
                music.imageUri = ContentUris.withAppendedId(
                    Uri.parse("content://media/external/audio/albumart"),
                    cursor.getInt(0).toLong()
                ).toString()
                musicList.add(music)
            }
        }
        return musicList
    }
}