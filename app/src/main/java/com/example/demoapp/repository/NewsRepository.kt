package com.example.demoapp.repository

import com.example.demoapp.api.RetrofitInstance

class NewsRepository {
    suspend fun getNewsData(
        q:String,
        from:String,
        sortBy:String,
        apiKey:String,
        baseUrl: String,
    ) = RetrofitInstance.api(baseUrl).getNewsData(q,from,sortBy,apiKey)
}