package com.example.notificationplanner.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class WeatherNotification {

    /*private fun instance(context : Context){
        val channel = NotificationChannel("channelId123", "test_channel", NotificationManager.IMPORTANCE_HIGH).apply {
            description = "notification description text"
        }
        val notificationManager: NotificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val not = NotificationCompat.Builder(context, "channelId123")
            //.setSmallIcon()
            .setContentTitle("Test Title")
            .setContentText("Test Text")
            .setPriority(NotificationCompat.PRIORITY_HIGH)



            with(NotificationManagerCompat.from(context)) {
                notify(123, not.build())

        }
    }*/

}