package com.example.notificationplanner.utils

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notificationplanner.R
import com.example.notificationplanner.notifications.NotificationService

class ExceptionNotification {
    companion object {
        fun create(title: String, msg: String, context: Context): Notification {
            return NotificationCompat.Builder(context, NotificationService.PLANNER_CHANNEL_ID)
                //TODO replace with final icon
                .setSmallIcon(R.drawable.img_da)
                //.setLargeIcon(Bitmap.createBitmap())
                .setContentTitle(title)
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()
        }
        fun default(context: Context) :  Notification{
            return NotificationCompat.Builder(context, NotificationService.PLANNER_CHANNEL_ID)
                //TODO replace with final icon
                .setSmallIcon(R.drawable.img_da)
                //.setLargeIcon(Bitmap.createBitmap())
                .setContentTitle("Fail")
                .setContentText("Something went wrong")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()
        }
        fun sendExceptionNotification(context: Context, msg: String? = null ) {
            val notification = if (msg != null) {
                create(
                    "Fail",
                    msg,
                    context
                )
            } else {
                default(context)
            }
            with(NotificationManagerCompat.from(context)) {
                notify(1, notification)
            }
        }
    }
}