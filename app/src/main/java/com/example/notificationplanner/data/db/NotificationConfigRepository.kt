package com.example.notificationplanner.data.db

import android.content.Context
import com.example.notificationplanner.data.NotificationConfig

class NotificationConfigRepository(context: Context) {

    private val dao = NotificationDatabase.getDatabase(context).notificationConfigDao()

    val readAllData: List<NotificationConfig> = dao.readAllNotificationConfigurations()


    // TODO absturz kp
    suspend fun findById(id: Int): NotificationConfig? {
        return dao.findNotificationConfigById(id)!!
    }

    suspend fun addNotificationConfig(notificationConfig: NotificationConfig) {
        dao.addNotificationConfiguration(notificationConfig)
    }

    suspend fun deleteNotificationConfig(notificationConfig: NotificationConfig) {
        dao.deleteNotificationConfiguration(notificationConfig)
    }

}