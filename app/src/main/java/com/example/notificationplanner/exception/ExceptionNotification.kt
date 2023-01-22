package com.example.notificationplanner.exception

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notificationplanner.R
import com.example.notificationplanner.notifications.NotificationService

class ExceptionNotification {
    companion object {
        private fun create(title: String, msg: String, context: Context): Notification {
            return NotificationCompat.Builder(context, NotificationService.PLANNER_CHANNEL_ID)

                .setSmallIcon(R.drawable.keiho_icon)
                //.setLargeIcon(Bitmap.createBitmap())
                .setContentTitle(title)
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()
        }

        private fun default(context: Context) : Notification {
            return NotificationCompat.Builder(context, NotificationService.PLANNER_CHANNEL_ID)
                .setSmallIcon(R.drawable.keiho_icon)
                //.setLargeIcon(Bitmap.createBitmap())
                .setContentTitle("Fail")
                .setContentText("Something went wrong")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()
        }

        @SuppressLint("MissingPermission")
        fun sendExceptionNotification(context: Context, msg: String? = null) {
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