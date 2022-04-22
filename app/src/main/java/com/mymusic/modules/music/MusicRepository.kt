package com.mymusic.modules.music

import android.os.Build
import android.text.Html
import androidx.annotation.WorkerThread
import androidx.core.text.HtmlCompat
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mymusic.AppContainer
import com.mymusic.modules.search.MusicSearch
import com.mymusic.modules.search.searchSongDataset

class MusicRepository(
    private val musicApi: MusicApi = AppContainer.musicApi,
    private val musicDao: MusicDao = AppContainer.musicDao
) {
    suspend fun getBestMatchMusicList(names: List<String>) : List<Music>{
        val musicList = mutableListOf<Music>()
        for (name in names) {
            searchMusicMetaJsonList(name)?.bestMatchMusicMetaJson()?.toMusicSearch(name)
                ?.let { musicSearch ->
                    getMusic(musicSearch)?.let { music ->
                        musicList.add(music)
                    }
                }
        }
        return musicList
    }

    suspend fun getSearchList(search: String): List<MusicSearch> {
        val searchList = mutableListOf<MusicSearch>()
        try {
            val names = searchSongDataset(search)
            for (name in names) {
                searchMusicMetaJsonList(name)?.bestMatchMusicMetaJson()?.toMusicSearch(name)
                    ?.let { musicSearch ->
                        searchList.add(musicSearch)
                    }
            }
        } catch (e: Exception) {
        }
        return searchList
    }

    suspend fun getMusic(id: Long) = loadMusicEntityById(id)

    suspend fun getMusics(ids: List<Long>) = loadMusicEntitiesById(ids)

    suspend fun getMusic(musicSearch: MusicSearch): Music? = with(musicSearch) {
        loadMusicEntityByNameAndArtist(musicSearch)?.let { musicEntity ->
            return musicEntity.toMusic()
        }
        getMusicJson(musicSearch.id)?.toMusic(musicSearch.name)?.let { music ->
            music.id = insertMusicEntity(music.toMusicEntity())
            return music
        }
        return null
    }

    @WorkerThread
    private suspend fun insertMusicEntity(musicEntity: MusicEntity) =
        musicDao.insert(musicEntity)

    @WorkerThread
    private suspend fun loadMusicEntityByNameAndArtist(musicSearch: MusicSearch) =
        musicDao.loadMusicEntityByNameAndArtist(musicSearch.name, musicSearch.artist)

    @WorkerThread
    private suspend fun loadMusicEntityById(id: Long) =
        musicDao.loadMusicEntityById(id)

    @WorkerThread
    private suspend fun loadMusicEntitiesById(ids: List<Long>) =
        musicDao.loadMusicEntitiesById(ids)

    suspend fun deleteAll() {
        musicDao.deleteAll()
    }

    private suspend fun searchMusicMetaJsonList(search: String) = try {
        musicApi.search(text = search)
            .getAsJsonObject("songs")
            .getAsJsonArray("data")
    } catch (e: Exception) {
        null
    }

    private suspend fun getMusicJson(id: String) = try {
        musicApi.getMusicData(pids = id).getAsJsonObject(id)
    } catch (e: Exception) {
        null
    }

    private fun JsonArray.bestMatchMusicMetaJson() = try {
        this[0].asJsonObject
    } catch (e: Exception) {
        null
    }

    private fun JsonObject.toMusicSearch(name: String) = try {
        val id = this.get("id").asString
//        val name = this.get("title").asString.fromHtml()
        val artist = this.getAsJsonObject("more_info").get("primary_artists").asString.fromHtml()
        MusicSearch(id, name, artist)
    } catch (e: Exception) {
        null
    }

    private fun JsonObject.toMusic(name: String) = try {
        val image = this.get("image").asString
        val artist = this.get("primary_artists").asString.fromHtml()
        var path = this.get("media_preview_url").asString.replace("preview", "aac")
        path = path.replace("_96_p.mp4", "_160.mp4")
        Music(0, path, name, artist, image)
    } catch (e: Exception) {
        null
    }

    private fun String.fromHtml() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
    } else {
        Html.fromHtml(this).toString()
    }

}

