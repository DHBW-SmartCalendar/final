package com.example.notificationplanner.notifications.builder

import android.Manifest
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.notificationplanner.R
import com.example.notificationplanner.data.NotificationConfig
import com.example.notificationplanner.data.db.NotificationConfigRepository
import com.example.notificationplanner.externAPI.APIClient
import com.example.notificationplanner.externAPI.json.weather.Weather
import com.example.notificationplanner.externAPI.json.weather.WeatherInformation
import com.example.notificationplanner.notifications.NotificationService
import com.example.notificationplanner.utils.ExceptionNotification
import com.example.notificationplanner.utils.ExceptionNotification.Companion.sendExceptionNotification
import com.example.notificationplanner.utils.InternetConnection
import kotlinx.coroutines.*
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class WeatherNotification : BroadcastReceiver() {


    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context, intent: Intent?) {
        val uid = intent?.getIntExtra("uid", -1)
        Log.d(this@WeatherNotification.javaClass.name, "Received Intent with : Extra $uid")

        var notification: Notification?
        GlobalScope.launch(Dispatchers.IO) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                // DB request & API call
                if (uid != -1) {
                    val repoConfig = NotificationConfigRepository(context)
                    val config = repoConfig.findById(uid!!)
                    if (config != null) {
                        if (InternetConnection.check(context)) {
                            APIClient.request(config.type) {

                                //TODO Weather with default location
                                GlobalScope.launch(Dispatchers.IO) {
                                    val response = it.getWeather()
                                    notification = if (response.isSuccessful) {
                                        Log.d(this@WeatherNotification::class.java.name, "Weather Api request was successful")
                                        val weather = getCurrentWeather(response.body()!!)

                                        NotificationCompat.Builder(context, NotificationService.PLANNER_CHANNEL_ID)
                                            .setSmallIcon(R.drawable.img_da)
                                            //.setLargeIcon(Bitmap.createBitmap())
                                            .setContentTitle("Weather")
                                            .setStyle(NotificationCompat.BigTextStyle().bigText(getWeatherString(config, weather!!)))
                                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                                            .build()
                                    } else {
                                        Log.e(this@WeatherNotification.javaClass.name, "Api call failure ")
                                        ExceptionNotification.default(context)
                                    }

                                    with(NotificationManagerCompat.from(context)) {
                                        notify(1, notification!!)
                                    }
                                }
                            }
                        } else {
                            Log.e(this@WeatherNotification.javaClass.name, "Internet connection not available")
                            sendExceptionNotification(context, "To receive weather notifications your device needs to be connected to the internet")
                        }
                    } else {
                        Log.e(this@WeatherNotification.javaClass.name, "Config not found in db")
                        sendExceptionNotification(context)
                    }
                } else {
                    Log.e(this@WeatherNotification.javaClass.name, "Intent Extras check failed ::: value -> $uid")
                    sendExceptionNotification(context)
                }
            } else {
                Log.d(this.javaClass.name, "Permissions not granted")
                //sendExceptionNotification(context, "For receiving notifications from us, please accept the permissions in the settings!")
                //TODO show dialog / snackbar
            }
        }
        Log.d(this.javaClass.name, "Send Notification finally")
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

