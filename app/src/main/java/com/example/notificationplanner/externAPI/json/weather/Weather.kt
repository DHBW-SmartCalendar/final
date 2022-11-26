package com.example.notificationplanner.externAPI.json.weather

data class Weather(
    val cloud_cover: Int,
    val condition: String,
    val dew_point: Double,
    val icon: String,
    val precipitation: Double,
    val pressure_msl: Double,
    val relative_humidity: Int,
    val source_id: Int,
    val sunshine: Double,
    val temperature: Double,
    val timestamp: String,
    val visibility: Int,
    val wind_direction: Int,
    val wind_gust_direction: Any,
    val wind_gust_speed: Double,
    val wind_speed: Double
)