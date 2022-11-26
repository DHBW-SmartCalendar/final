package com.example.notificationplanner.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.notificationplanner.data.NotificationConfig
import com.example.notificationplanner.data.NotificationType
import com.example.notificationplanner.notifications.builder.*

class IntentProvider {
    companion object {

        fun <T> pendingIntentBroadCast(context: Context, requestCode: Int, destination: Class<T>): PendingIntent {
            return PendingIntent.getBroadcast(context, requestCode, Intent(context, destination), PendingIntent.FLAG_IMMUTABLE)
        }

        fun <T> pendingIntentBroadCast(context: Context, notificationConfig: NotificationConfig, destination: Class<T>): PendingIntent {
            val i = Intent(context, destination)
            i.putExtra("uid", notificationConfig.uid)
            return PendingIntent.getBroadcast(context, notificationConfig.uid, i, PendingIntent.FLAG_IMMUTABLE)
        }

        fun pendingIntentBroadCast(context: Context, notificationConfig: NotificationConfig): PendingIntent {
            return PendingIntent.getBroadcast(context, notificationConfig.uid, getIntent(context, notificationConfig), PendingIntent.FLAG_IMMUTABLE)
        }

        fun pendingIntentBroadCast(context: Context, requestCode: Int, intent: Intent): PendingIntent {
            return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE)
        }


        private fun getIntent(context: Context, notificationConfig: NotificationConfig): Intent {
            when (notificationConfig.type) {
                NotificationType.WEATHER -> {
                    return Intent(context, WeatherNotification::class.java).putExtra("uid", notificationConfig.uid)
                }
                NotificationType.EXCUSE -> {
                    return Intent(context, ExcuseNotification::class.java).putExtra("uid", notificationConfig.uid)
                }
                NotificationType.MEME -> {
                    return Intent(context, MemeNotification::class.java).putExtra("uid", notificationConfig.uid)
                }
                NotificationType.CALENDAR -> {
                    return Intent(context, CalendarNotification::class.java).putExtra("uid", notificationConfig.uid)
                }
                NotificationType.NEWS -> {
                    return Intent(context, NewsNotification::class.java).putExtra("uid", notificationConfig.uid)
                }
            }
        }
    }
}