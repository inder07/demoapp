package com.example.demoapp.api

import com.example.demoapp.models.ResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("everything")
    suspend fun getNewsData(@Query("q") q:String, @Query("from")from:String, @Query("sortBy")sortBy:String, @Query("apiKey")apiKey:String):Response<ResponseModel>
}