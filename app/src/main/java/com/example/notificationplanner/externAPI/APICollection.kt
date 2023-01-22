package com.example.notificationplanner.externAPI

import com.example.notificationplanner.externAPI.json.excuses.Excuse
import com.example.notificationplanner.externAPI.json.meme.Meme
import com.example.notificationplanner.externAPI.json.news.News
import com.example.notificationplanner.externAPI.json.weather.WeatherInformation
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDate

interface APICollection {
    @GET("weather")
    suspend fun getWeather(
        @Query("lat") lat: Double = 49.48,
        @Query("lon") lon: Double = 8.51,
        @Query("date") date: String = LocalDate.now().toString()
    ): Response<WeatherInformation>

    @GET("excuse/{category}/{amount}")
    suspend fun getExcuse(
        @Path("category") category: String,
        @Path("amount") amount: Int
    ): Response<Excuse>

    @GET("top-headlines")
    suspend fun getNews(
        @Query("category") category: String = "health",
        @Query("country") country: String = "de",
        @Query("apiKey") apiKey: String = "61d9c36a22d64a57a58473bcd131aa04",
    ): Response<News>

    @GET("gimme/{count}")
    suspend fun getMeme(
        @Path("count") count: Int
    ): Response<Meme>
}