package com.example.notificationplanner.data.db

import android.content.Context
import com.example.notificationplanner.data.NotificationConfig

class NotificationRepository(context: Context) {

    private val notificationConfigurationDAO = NotificationDatabase.getDatabase(context).notificationDao()

    val readAllData: List<NotificationConfig> = notificationConfigurationDAO.readAllNC()

    suspend fun add(notificationConfig: NotificationConfig) {
        notificationConfigurationDAO.addNotificationConfiguration(notificationConfig)
    }
}