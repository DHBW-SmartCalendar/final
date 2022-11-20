package com.example.notificationplanner

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.example.notificationplanner.notifications.NotificationService.Companion.PLANNER_CHANNEL_ID

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        println("Application started +++++++++++++++++++++++++++++++++++++++++++++++++++")
        createNotificationChannel()

    }
    private fun createNotificationChannel(){
        val channel = NotificationChannel(
            PLANNER_CHANNEL_ID,
            "Planner",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    override fun onTerminate() {
        super.onTerminate()
        println("Application terminated +++++++++++++++++++++++++++++++++++++++++++++++++++")

    }
}
