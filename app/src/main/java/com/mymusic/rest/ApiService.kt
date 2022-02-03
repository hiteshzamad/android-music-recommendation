package com.mymusic.rest

import com.google.gson.JsonObject
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("predict")
    suspend fun getRecommends(@Body songs: JsonObject): JsonObject
}