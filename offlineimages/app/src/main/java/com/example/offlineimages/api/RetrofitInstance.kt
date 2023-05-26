package com.example.offlineimages.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object {
        private var baseUrl = ""
        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            val gson = GsonBuilder()
                .setLenient()
                .create()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
        }

        fun api(baseUrl: String): ApiInterface {
            this.baseUrl = baseUrl
            return retrofit.create(ApiInterface::class.java)
        }
    }
}
