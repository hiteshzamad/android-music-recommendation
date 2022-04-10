package com.mymusic.config

import com.mymusic.modules.recommendation.RecommendationApiService
import com.mymusic.modules.song.SongApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitConfiguration {
    private val RECOMMENDATION_BASE_URL = "https://music-recommender-groupid-5.herokuapp.com/"
    private val SONG_BASE_URL = "https://www.jiosaavn.com/"
    fun recommendationApiService(): RecommendationApiService {
        return Retrofit.Builder()
            .baseUrl(RECOMMENDATION_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RecommendationApiService::class.java)
    }

    fun songApiService() : SongApiService{
        return Retrofit.Builder()
            .baseUrl(SONG_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SongApiService::class.java)
    }
}