package com.example.offlineimages.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.offlineimages.models.Data

@Dao
interface AnimeDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAnimeDetails(event: ArrayList<Data>)

    @Query("SELECT * FROM anime_details")
    fun getAnimeDetails(): List<Data>
}