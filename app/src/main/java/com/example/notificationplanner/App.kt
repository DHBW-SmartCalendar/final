package com.example.notificationplanner

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.example.notificationplanner.jobs.SyncScheduledNotificationsJob
import com.example.notificationplanner.notifications.NotificationService.Companion.PLANNER_CHANNEL_ID

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        SyncScheduledNotificationsJob.registerDailySync(applicationContext)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            PLANNER_CHANNEL_ID,
            "Planner",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }


}
