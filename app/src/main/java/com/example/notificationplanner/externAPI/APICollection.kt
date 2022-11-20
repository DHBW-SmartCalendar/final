package com.example.notificationplanner.externAPI

import com.example.notificationplanner.externAPI.json.weather.WeatherInformation
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.LocalDate

interface APICollection {
    @GET("weather?lat=49.47&lon=8.54&date=2022-11-17")
    suspend fun getWeather(
        @Query("lat") lat: Double = 49.48,
        @Query("lon") lon: Double = 8.51,
        @Query("date") date: String = LocalDate.now().toString()
    ): Response<WeatherInformation>
}