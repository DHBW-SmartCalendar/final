package com.example.notificationplanner.z_old

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.notificationplanner.data.NotificationConfig
import com.example.notificationplanner.data.db.NotificationConfigRepository
import com.example.notificationplanner.externAPI.APIClient
import com.example.notificationplanner.externAPI.json.weather.Weather
import com.example.notificationplanner.externAPI.json.weather.WeatherInformation
import com.example.notificationplanner.utils.InternetConnection
import kotlinx.coroutines.*
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class WeatherAPIJob : BroadcastReceiver() {

    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context, intent: Intent?) {
        val uid = intent?.getIntExtra("uid", -1)
        //val time = intent?.getLongExtra("time", 1L)
        Log.d(this@WeatherAPIJob.javaClass.name, "Intent received :::: Extra $uid")


        if (uid != -1) {
            GlobalScope.launch(Dispatchers.IO) {
                val repoConfig = NotificationConfigRepository(context = context)
                val config = repoConfig.findById(uid!!)
                if (config != null) {
                    if (InternetConnection.check(context)) {
                        APIClient.request(config.type) {
                            GlobalScope.launch(Dispatchers.IO) {
                                //TODO Weather with default location
                                //TODO check for internet connection

                                val asyncResponse = async { it.getWeather() }
                                val response = asyncResponse.await()

                                if (response.isSuccessful) {
                                    Log.d(this@WeatherAPIJob::class.java.name, "Weather Api request was successful")
                                    val weather = getCurrentWeather(response.body()!!)
                                    print(response.body().toString())
                                    println(getWeatherString(config, weather!!))
                                } else {
                                    Log.e(this@WeatherAPIJob.javaClass.name, "Api call failure ")
                                }
                            }
                        }
                    } else {
                        Log.e(this@WeatherAPIJob.javaClass.name, "Internet connection not available")
                    }
                } else {
                    Log.e(this@WeatherAPIJob.javaClass.name, "Config not found in db  ")
                }
            }
        } else {
            Log.e(this@WeatherAPIJob.javaClass.name, "Intent Extras check failed ::: value -> $uid")
        }
    }

    private fun List<NotificationConfig>.byId(id: Int): NotificationConfig? {
        forEach { if (it.uid == id) return it }
        return null
    }

    private fun getCurrentWeather(weatherInformation: WeatherInformation): Weather? {
        weatherInformation.weather.forEach {
            if (getTime(it).hour == LocalTime.now().hour) {
                return it
            }
        }
        return null
    }

    private fun getTime(weather: Weather): LocalTime {
        val w = weather.timestamp.split(Regex("T")).get(1).subSequence(0, 5)
        return LocalTime.parse(w, DateTimeFormatter.ofPattern("HH:mm"))
    }

    private fun getWeatherString(config: NotificationConfig, weather: Weather): String {

        val str = buildString {
            if (config.weather_cloud_cover) {
                append("Cloud over        : ${weather.cloud_cover} \n")
            }
            if (config.weather_temperature) {
                append("Temperature       : ${weather.temperature} \n")
            }
            if (config.weather_dew_point) {
                append("Dew Point         : ${weather.dew_point} \n")
            }
            if (config.weather_precipitation) {
                append("Precipitation     : ${weather.precipitation} \n")
            }
            if (config.weather_visibility) {
                append("Visibility        : ${weather.visibility} \n")
            }
            if (config.weather_relative_humidity) {
                append("Relative humidity : ${weather.relative_humidity} \n")
            }
            if (config.weather_sunshine) {
                append("Sun shine         : ${weather.sunshine} \n")
            }
            if (config.weather_wind_direction) {
                append("Wind direction    : ${weather.wind_direction} \n")
            }
            if (config.weather_wind_speed) {
                append("Wind Speed        : ${weather.wind_speed} \n")
            }
        }

        return str
    }
}

