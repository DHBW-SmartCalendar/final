package com.example.notificationplanner.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "notification_configuration")
data class NotificationConfig(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    var isActive: Boolean = false,
    var listenOnAlarm: Boolean = false,
    var listenOnCalendar: Boolean = false,
    var listenOnOwnTimer: Boolean = false,
    var timerTime: String? = null,
    var type: NotificationType = NotificationType.WEATHER,

    var weather_cloud_cover: Boolean = false,
    var weather_dew_point: Boolean = false,
    var weather_precipitation: Boolean = false,
    var weather_relative_humidity: Boolean = false,
    var weather_sunshine: Boolean = false,
    var weather_temperature: Boolean = false,
    var weather_visibility: Boolean = false,
    var weather_wind_direction: Boolean = false,
    var weather_wind_speed: Boolean = false,

    // TODO replace strings with enum
    var news_topic: String = "",
    var news_amount: Int = 1,

    var excuses_category: String = "",
    var excuses_amount: Int = 1,

    var calendar_next_events_amount: Int = 1


): Serializable