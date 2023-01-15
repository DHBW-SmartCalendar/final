package com.example.notificationplanner.notifications.builder

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notificationplanner.R
import com.example.notificationplanner.data.NotificationConfig
import com.example.notificationplanner.exception.ExceptionNotification
import com.example.notificationplanner.externAPI.json.weather.Weather
import com.example.notificationplanner.externAPI.json.weather.WeatherInformation
import com.example.notificationplanner.notifications.NotificationService
import com.example.notificationplanner.utils.NotificationsConditions
import kotlinx.coroutines.*
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class WeatherNotification : BroadcastReceiver() {

    @SuppressLint("MissingPermission")
    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context, intent: Intent?) {
         val uid = intent?.getIntExtra("uid", -1)
        Log.d(this@WeatherNotification.javaClass.name, "Received Intent with : Extra $uid")

        NotificationsConditions.check(context, uid!!) { api, config ->
            //TODO Weather depending on users location
            GlobalScope.launch(Dispatchers.IO) {
                val response = api.getWeather()
                if (response.isSuccessful) {
                    Log.d(this@WeatherNotification::class.java.name, "Weather Api request was successful")
                    val weather = getCurrentWeather(response.body()!!)

                    val notification = NotificationCompat.Builder(context, NotificationService.PLANNER_CHANNEL_ID)
                        .setSmallIcon(R.drawable.img_da)
                        //.setLargeIcon(Bitmap.createBitmap())
                        .setContentTitle("Weather")
                        .setStyle(NotificationCompat.BigTextStyle().bigText(getWeatherString(config, weather!!)))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .build()

                    with(NotificationManagerCompat.from(context)) {
                        notify(1, notification)
                    }
                    Log.d(this@WeatherNotification::class.java.name, "Notification sent finally -> config uid : $uid")
                } else {
                    Log.e(this@WeatherNotification.javaClass.name, "Api call failure ")
                    ExceptionNotification.default(context)
                }
            }
        }
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

