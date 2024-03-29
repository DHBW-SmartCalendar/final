package com.example.notificationplanner.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.notificationplanner.externAPI.json.excuses.Excuse_Category
import com.example.notificationplanner.externAPI.json.news.NewsCategory
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

    var news_category: NewsCategory = NewsCategory.BUSINESS,
    var news_amount: Int = 1,
    var news_country: Country = Country.GERMANY,

    var excuses_category: Excuse_Category = Excuse_Category.FAMILY,
    var excuses_amount: Int = 1,

    var meme_amount : Int = 1,

    var calendar_next_events_amount: Int = 1


// !!! After changing something in this class you need to increase the database version with 1 -> NotificationDatabase


): Serializable