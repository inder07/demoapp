package com.example.offlineimages.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anime_details")
data class Data(
    @PrimaryKey
    val _id: String,
    val episodes: Int,
    val hasEpisode: Boolean,
    val hasRanking: Boolean,
    val image: String,
    val link: String,
    val ranking: Int,
    val status: String,
    val synopsis: String,
    val thumb: String,
    val title: String,
    val type: String
)