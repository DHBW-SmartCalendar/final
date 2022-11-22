package com.example.notificationplanner.notifications

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.notificationplanner.R

class WeatherNotification : BroadcastReceiver() {


    override fun onReceive(p0: Context, p1: Intent?) {
        val notification = NotificationCompat.Builder(p0, NotificationService.PLANNER_CHANNEL_ID)
            .setSmallIcon(R.drawable.alarm)
            .setContentTitle("Test Title")
            .setContentText("Test Text")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        Log.i(this.javaClass.name, "received Notification at scheduler")
        if (ContextCompat.checkSelfPermission(p0, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            with(NotificationManagerCompat.from(p0)) {
                notify(123, notification)
                Log.d(this.javaClass.name, "Send Notification finally")

            }
        }else {
            Log.d(this.javaClass.name, "Permissions not granted")
        }
    }
}

