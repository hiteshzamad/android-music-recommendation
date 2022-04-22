package com.mymusic.modules.music

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicDao {

    @Query("SELECT * FROM music")
    fun loadAll(): Flow<List<MusicEntity>>

    @Insert
    suspend fun insert(musicEntity: MusicEntity): Long

    @Query("SELECT * FROM music WHERE name = :name AND artist = :artist LIMIT 1")
    suspend fun loadMusicEntityByNameAndArtist(name: String, artist: String): MusicEntity?

    @Query("SELECT * FROM music WHERE id = :id")
    suspend fun loadMusicEntityById(id: Long): MusicEntity?

    @Query("SELECT * FROM music WHERE id IN (:ids)")
    suspend fun loadMusicEntitiesById(ids: List<Long>): List<MusicEntity>

    @Query("DELETE FROM music")
    suspend fun deleteAll()

}