package com.example.notificationplanner.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.notificationplanner.data.db.NotificationTrigger
import java.io.Serializable


@Entity(tableName = "scheduled_notification")
data class ScheduledNotification(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    val notificationType: NotificationType,
    val notificationTrigger: NotificationTrigger?,
    val time: String?,
    val isDaily: Boolean,
    val notificationConfigUid: Int
) : Serializable
