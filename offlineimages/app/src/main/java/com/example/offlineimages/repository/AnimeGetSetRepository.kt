package com.example.offlineimages.repository

import com.example.offlineimages.api.RetrofitInstance

class AnimeGetSetRepository {
    suspend fun getSetAnimeDetails(
        size:Int,
        page:Int,
        baseUrl: String,
    ) = RetrofitInstance.api(baseUrl).getAllAnime(page,size)
}