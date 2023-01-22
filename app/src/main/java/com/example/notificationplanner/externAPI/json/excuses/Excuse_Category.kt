package com.example.notificationplanner.externAPI.json.excuses

import com.example.notificationplanner.ui.components.DropDownCompatible

enum class Excuse_Category(val description: String): DropDownCompatible {
    FAMILY("Family") {
        override fun getLabelText(): String {
            return description
        }
    },
    OFFICE("Office") {
        override fun getLabelText(): String {
            return description
        }
    },
    CHILDREN("Children") {
        override fun getLabelText(): String {
            return description
        }
    },
    COLLEGE("College") {
        override fun getLabelText(): String {
            return description
        }
    },
    PARTY("Party") {
        override fun getLabelText(): String {
            return description
        }
    },
    FUNNY("Funny") {
        override fun getLabelText(): String {
            return description
        }
    },
    UNBELIEVABLE("Unbelievable") {
        override fun getLabelText(): String {
            return description
        }
    },
    DEVELOPERS("Developers") {
        override fun getLabelText(): String {
            return description
        }
    },
    GAMING("Gaming") {
        override fun getLabelText(): String {
            return description
        }
    },
}