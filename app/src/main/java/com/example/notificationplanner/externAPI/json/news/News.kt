package com.example.notificationplanner.externAPI.json.news

data class News(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)