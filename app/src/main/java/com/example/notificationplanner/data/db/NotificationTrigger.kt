package com.example.notificationplanner.data.db

import java.io.Serializable

enum class NotificationTrigger : Serializable {
    OWN_TIME,
    ALARM_CLOCK,
    CALENDAR
}