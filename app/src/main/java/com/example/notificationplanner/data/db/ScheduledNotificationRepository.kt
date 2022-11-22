package com.example.notificationplanner.data.db

import android.content.Context
import com.example.notificationplanner.data.ScheduledNotification

class ScheduledNotificationRepository(context: Context) {
    
    private val dao = NotificationDatabase.getDatabase(context).scheduledNotificationDao()

    val readAllData: List<ScheduledNotification> = dao.readAllScheduledNotifications()

    suspend fun findById(id: Int): ScheduledNotification? {
        return dao.findScheduledNotificationById(id)!!
    }

    suspend fun addScheduledNotification(scheduledNotification: ScheduledNotification) {
        dao.addScheduledNotification(scheduledNotification)
    }

    suspend fun deleteScheduledNotification(scheduledNotification: ScheduledNotification) {
        dao.deleteScheduledNotification(scheduledNotification)
    }
}