package com.mymusic.modules.musichistory

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicHistoryDao {

    @Query("SELECT * FROM musicHistory order by lastPlayed DESC LIMIT 10")
    fun loadRecent10(): Flow<List<MusicHistoryEntity>>

    @Query("SELECT * FROM musicHistory WHERE musicId = :id")
    suspend fun loadById(id: Long): MusicHistoryEntity?

    @Insert
    suspend fun insert(musicHistoryEntity: MusicHistoryEntity)

    @Update
    suspend fun update(musicHistoryEntity: MusicHistoryEntity)

    @Query("DELETE FROM musicHistory")
    suspend fun deleteAll()

}