package com.mymusic.config

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mymusic.modules.recommendation.RecommendationDao
import com.mymusic.modules.recommendation.RecommendationEntity

@Database(
    entities = [RecommendationEntity::class],
    version = 2
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun recommendationDao(): RecommendationDao

    companion object {
        @Volatile
        private var instance: LocalDatabase? = null

        fun getDatabase(
            context: Context
        ): LocalDatabase {
            return instance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalDatabase::class.java,
                    "main_database"
                ).fallbackToDestructiveMigration().build()
                this.instance = instance
                instance
            }
        }
    }
}