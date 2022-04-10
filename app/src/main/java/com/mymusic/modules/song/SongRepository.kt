package com.mymusic.modules.song

import com.google.gson.JsonObject
import com.mymusic.AppContainer
import com.mymusic.model.Music

class SongRepository(
    private val songApiService: SongApiService = AppContainer.songApiService
) {
    suspend fun getMusicList(names: List<String>): List<Music> {
        val musicList = mutableListOf<Music>()
        for (name in names) {
            val pid = getSongId(name)
            pid?.let { pid1 ->
                val s2 = songApiService.songData(pids = pid1)
                s2.toMusic(pid1, name)?.let {
                    musicList.add(it)
                }
            }
        }
        return musicList
    }

    private suspend fun getSongId(string: String): String? {
        return try {
            val songsJson = songApiService.search(text = string)
                .getAsJsonObject("songs")
                .getAsJsonArray("data")
            songsJson[0].asJsonObject.get("id").asString
        } catch (e: Exception) {
            null
        }
    }

    private fun JsonObject.toMusic(pid: String, name: String) = with(this.getAsJsonObject(pid)) {
        val image = get("image").asString
        val artist = get("music").asString
        val duration = get("duration").asFloat
        if (duration < 0)
            return@with null
        var path = get("media_preview_url").asString.replace("preview", "aac")
        path = path.replace("_96_p.mp4", "_160.mp4")
        Music(path, name, artist, image, duration)
    }
}

