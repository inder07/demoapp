package com.example.offlineimages.api

import com.example.offlineimages.models.AnimeDbModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiInterface {
    @Headers(
        "X-RapidAPI-Key:6d83e02924msh4fad15ba98a366ep111b1ajsn05174e5cefa7",
        "X-RapidAPI-Host:anime-db.p.rapidapi.com")
    @GET("anime")
    suspend fun getAllAnime(@Query("page") page:Int, @Query("size")size:Int): Response<AnimeDbModel>
}