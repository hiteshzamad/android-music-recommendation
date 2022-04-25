package com.mymusic.modules.music

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mymusic.AppContainer
import com.mymusic.modules.search.MusicSearch

class MusicRepository(
    private val musicApi: MusicApi = AppContainer.musicApi,
    private val musicCollection: MusicCollection = AppContainer.musicCollection
) {
    suspend fun getMusicListByNames(searches: List<String>): List<Music> {
        val musics = mutableListOf<Music>()
        for (search in searches) {
            val document = musicCollection.searchByName(search)
            document?.let { document1 ->
                val music = getMusicByMusicSearch(document1.id, document1.name, document1.artist)
                music?.let { music1 -> musics.add(music1) }
            }
        }
        return musics
    }

    suspend fun getMusicById(id: String): Music? {
        musicCollection.searchById(id)?.let { document ->
            return getMusicByMusicSearch(document.id, document.name, document.artist)
        }
        return null
    }

    suspend fun getMusicSearchList(search: String): List<MusicSearch> {
        val searchList = mutableListOf<MusicSearch>()
        try {
            val documents = musicCollection.queryByName(search)
            for (document in documents) {
                searchList.add(MusicSearch(document.id, document.name, document.artist))
            }
        } catch (e: Exception) {
        }
        return searchList
    }


    suspend fun getMusicByMusicSearch(id: String, name: String, artist: String): Music? {
        try {
            musicApi.search(text = name)
                .getAsJsonObject("songs")
                .getAsJsonArray("data")?.bestMatchMusicApiId(
                    name, artist
                )?.let { apiId ->
                    getMusicJsonByApiId(apiId)?.toImageUrlPair()?.let { pair ->
                        return Music(
                            id,
                            pair.second,
                            name,
                            artist,
                            pair.first
                        )
                    }
                }
            return null
        }catch (e: Exception){
            return null
        }
    }

    private suspend fun getMusicJsonByApiId(id: String): JsonObject? = try {
        musicApi.getMusicData(pids = id).getAsJsonObject(id)
    } catch (e: Exception) {
        null
    }

    private fun JsonArray.bestMatchMusicApiId(name: String, artist: String) = try {
        this[0].asJsonObject?.get("id")?.asString
    } catch (e: Exception) {
        null
    }

    private fun JsonObject.toImageUrlPair() = try {
        val image = this.get("image").asString
        var path = this.get("media_preview_url").asString.replace("preview", "aac")
        path = path.replace("_96_p.mp4", "_160.mp4")
        Pair(image, path)
    } catch (e: Exception) {
        null
    }
}

