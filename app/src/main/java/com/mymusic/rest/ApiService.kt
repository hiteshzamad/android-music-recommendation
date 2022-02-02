package com.mymusic.rest

//import com.mindorks.retrofit.coroutines.data.model.User import recommends model
import com.google.firebase.firestore.auth.User
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("")
    suspend fun getRecommends(@Body songs: List<String>): List<String>

}