package com.example.notificationplanner.externAPI.json.weather

data class WeatherInformation(
    val sources: List<Source>,
    val weather: List<Weather>
)