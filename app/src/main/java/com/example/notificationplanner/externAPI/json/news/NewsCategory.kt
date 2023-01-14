package com.example.notificationplanner.externAPI.json.news

import com.example.notificationplanner.ui.components.DropDownCompatible

enum class NewsCategory(val description: String) : DropDownCompatible {
    BUSINESS("business") {
        override fun getLabelText(): String {
            return description
        }
    },
    ENTERTAINMENT("entertainment") {
        override fun getLabelText(): String {
            return description
        }
    },
    GENERAL("general") {
        override fun getLabelText(): String {
            return description
        }
    },
    HEALTH("health") {
        override fun getLabelText(): String {
            return description
        }
    },
    SCIENCE("science") {
        override fun getLabelText(): String {
            return description
        }
    },
    SPORTS("sports") {
        override fun getLabelText(): String {
            return description
        }
    },
    TECHNOLOGY("technology") {
        override fun getLabelText(): String {
            return description
        }
    },
}