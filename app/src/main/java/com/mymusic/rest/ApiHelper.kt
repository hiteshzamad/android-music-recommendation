package com.mymusic.rest

class ApiHelper(private val apiService: ApiService)  {
    suspend fun getUsers() = apiService.getRecommends()
}