package com.example.notificationplanner.data

import com.example.notificationplanner.ui.components.DropDownCompatible

enum class Country(val str : String) :DropDownCompatible {
    GERMANY("de") {
        override fun getLabelText(): String {
          return "Germany"
        }
    },
    USA("us") {
        override fun getLabelText(): String {
            return "USA"
        }
    },
    CANADA("ca") {
        override fun getLabelText(): String {
            return "Canada"
        }
    },
    JAPAN("jp") {
        override fun getLabelText(): String {
            return "Japan"
        }
    },
    CHINA("cn") {
        override fun getLabelText(): String {
            return "China"
        }
    },
    NETHERLANDS("nl") {
        override fun getLabelText(): String {
            return "Netherlands"
        }
    },
    GREAT_BRITAIN("gb") {
        override fun getLabelText(): String {
            return "Great Britain"
        }
    };


}