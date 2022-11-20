package com.example.notificationplanner.externAPI.json.weather

data class Source(
    val distance: Double,
    val dwd_station_id: String,
    val first_record: String,
    val height: Double,
    val id: Int,
    val last_record: String,
    val lat: Double,
    val lon: Double,
    val observation_type: String,
    val station_name: String,
    val wmo_station_id: String
)