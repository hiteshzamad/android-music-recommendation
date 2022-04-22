package com.mymusic.config

import com.mymusic.modules.recommendation.RecommendationApi
import com.mymusic.modules.music.MusicApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitConfiguration {
    private val RECOMMENDATION_BASE_URL = "https://music-recommender-groupid-5.herokuapp.com/"
    private val SONG_BASE_URL = "https://www.jiosaavn.com/"
    fun recommendationApiService(): RecommendationApi {
        return Retrofit.Builder()
            .baseUrl(RECOMMENDATION_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RecommendationApi::class.java)
    }

    fun songApiService() : MusicApi{
        return Retrofit.Builder()
            .baseUrl(SONG_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MusicApi::class.java)
    }
}