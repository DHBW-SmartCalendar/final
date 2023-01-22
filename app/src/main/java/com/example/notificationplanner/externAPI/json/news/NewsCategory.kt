package com.example.notificationplanner.externAPI.json.news

import com.example.notificationplanner.ui.components.DropDownCompatible

enum class NewsCategory(val description: String) : DropDownCompatible {
    BUSINESS("business") {
        override fun getLabelText(): String {
            return description.str()
        }
    },
    ENTERTAINMENT("entertainment") {
        override fun getLabelText(): String {
            return description.str()
        }
    },
    GENERAL("general") {
        override fun getLabelText(): String {
            return description.str()
        }
    },
    HEALTH("health") {
        override fun getLabelText(): String {
            return description.str()
        }
    },
    SCIENCE("science") {
        override fun getLabelText(): String {
            return description.str()
        }
    },
    SPORTS("sports") {
        override fun getLabelText(): String {
            return description.str()
        }
    },
    TECHNOLOGY("technology") {
        override fun getLabelText(): String {
            return description.str()
        }
    },;

    fun String.str(): String {
        return description[0].uppercase() + description.substring(1)
    }


}