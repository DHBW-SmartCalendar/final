package com.example.notificationplanner.data

import com.example.notificationplanner.ui.components.DropDownCompatible
import java.io.Serializable

enum class NotificationType( val description: String, val url: String? = null) : Serializable , DropDownCompatible {
    WEATHER("Weather", "https://api.brightsky.dev/") {
        override fun getLabelText(): String {
            return description
        }
    },
    NEWS("News", "https://newsapi.org/v2/") {
        override fun getLabelText(): String {
           return description
        }
    },
    MEME("Random Meme", "https://meme-api.herokuapp.com/") { //gimme
        override fun getLabelText(): String {
           return description
        }
    },
    EXCUSE("Random Excuse", "https://excuser.herokuapp.com/v1/") { //excuse
        override fun getLabelText(): String {
           return description
        }
    },
    CALENDAR("Calendar Events") {
        override fun getLabelText(): String {
           return description
        }
    }
}