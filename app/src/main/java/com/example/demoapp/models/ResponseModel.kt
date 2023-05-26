package com.example.demoapp.models

data class ResponseModel(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)