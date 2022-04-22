package com.mymusic.config

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mymusic.modules.music.MusicDao
import com.mymusic.modules.music.MusicEntity
import com.mymusic.modules.musichistory.MusicHistoryDao
import com.mymusic.modules.musichistory.MusicHistoryEntity
import com.mymusic.modules.recommendation.RecommendationDao
import com.mymusic.modules.recommendation.RecommendationEntity

@Database(
    entities = [MusicHistoryEntity::class, MusicEntity::class, RecommendationEntity::class],
    version = 2
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun musicHistoryDao(): MusicHistoryDao
    abstract fun recommendationDao(): RecommendationDao
    abstract fun musicDao(): MusicDao

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
                )
                    .fallbackToDestructiveMigration().build()
                this.instance = instance
                instance
            }
        }
    }
}