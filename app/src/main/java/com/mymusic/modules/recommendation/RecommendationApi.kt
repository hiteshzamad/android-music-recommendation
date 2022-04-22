package com.mymusic.modules.recommendation

import com.google.gson.JsonObject
import retrofit2.http.Body
import retrofit2.http.POST

interface RecommendationApi {
    @POST("predict")
    suspend fun recommends(@Body songs: JsonObject): JsonObject
}