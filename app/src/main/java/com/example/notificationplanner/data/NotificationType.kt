package com.example.notificationplanner.data

import java.io.Serializable

enum class NotificationType( val description: String, val url: String? = null) : Serializable {
    WEATHER("Weather", "https://api.brightsky.dev/"),
    NEWS("News", "https://newsapi.org/v2/"),
    MEME("Random Meme", "https://meme-api.herokuapp.com/"), //gimme
    EXCUSE("Random Excuse", "https://excuser.herokuapp.com/v1/"), //excuse
    CALENDAR("Calendar Events")
}