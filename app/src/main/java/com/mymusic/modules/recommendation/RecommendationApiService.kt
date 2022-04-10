package com.mymusic.modules.recommendation

import com.google.gson.JsonObject
import retrofit2.http.Body
import retrofit2.http.POST

interface RecommendationApiService {
    @POST("predict")
    suspend fun recommends(@Body songs: JsonObject): JsonObject
}