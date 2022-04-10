package com.mymusic.modules.song

import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.Query

interface SongApiService {

    @GET("api.php")
    suspend fun search(
        @Query("__call") call: String = "autocomplete.get",
        @Query("_format") format: String = "json",
        @Query("_marker") marker: String = "0",
        @Query("cc") cc: String = "in",
        @Query("includeMetaTags") includeMetaTags: String = "1",
        @Query("query") text: String
    ): JsonObject

    @GET("api.php")
    suspend fun songData(
        @Query("__call") call: String = "song.getDetails",
        @Query("_format") format: String = "json",
        @Query("_marker") marker: String = "0%3F_marker%3D0",
        @Query("cc") cc: String = "in",
        @Query("pids") pids: String
    ) : JsonObject
}