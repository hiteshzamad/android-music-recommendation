package com.mymusic.modules.recommendation

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mymusic.AppContainer

class RecommendationRepository(
    private val recommendationApi: RecommendationApi = AppContainer.recommendationApi,
    private val recommendationDao: RecommendationDao = AppContainer.recommendationDao
) {

    fun readAll() = recommendationDao.loadAll()

    suspend fun refreshRecommendation(list: List<String>): List<String> {
        return recommendationApi.recommends(list.getJsonObject()).getList()
    }

    suspend fun update(list: List<String>) {
        recommendationDao.deleteAll()
        recommendationDao.inserts(list.toRecommendationEntities())
    }

    suspend fun deleteAll() {
        recommendationDao.deleteAll()
    }

    private fun List<String>.getJsonObject(): JsonObject {
        val jsonObject = JsonObject()
        val jsonArray = JsonArray()
        this.forEach { jsonArray.add(it) }
        jsonObject.add("songs", jsonArray)
        return jsonObject
    }

    private fun JsonObject.getList(): List<String> {
        val list = mutableListOf<String>()
        val jsonArray = this.getAsJsonArray("predicted_songs")
        jsonArray.forEach {
            list.add(it.asString)
        }
        return list
    }

    private fun List<String>.toRecommendationEntities() : List<RecommendationEntity>{
        val list = mutableListOf<RecommendationEntity>()
        this.forEach { i ->
            list.add(RecommendationEntity(i))
        }
        return list
    }

}