package com.example.offlineimages.room.animedb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.offlineimages.models.Data
import com.example.offlineimages.room.dao.AnimeDetailsDao


@Database(entities = [Data::class], version = 1)
abstract  class AnimeSqlDb : RoomDatabase() {
    abstract fun animeDao():AnimeDetailsDao
    companion object{
        private var INSTANCE:AnimeSqlDb?=null
        fun getDatabase(context: Context):AnimeSqlDb{
            if(INSTANCE==null)
            {
                INSTANCE= Room.databaseBuilder(
                    context,
                    AnimeSqlDb::class.java,
                    "animeDb"
                ).build()
            }
            return INSTANCE!!
        }

    }
}


