package com.example.notificationplanner.notifications.scheduler

import android.app.PendingIntent
import android.content.Context
import android.content.Intent

class IntentProvider {
    companion object {

        fun <T> pendingIntentBroadCast(context: Context, requestCode: Int, destination: Class<T>): PendingIntent {
            return PendingIntent.getBroadcast(context, requestCode, Intent(context, destination), PendingIntent.FLAG_IMMUTABLE)
        }

        fun pendingIntentBroadCast(context: Context, requestCode: Int, intent: Intent): PendingIntent {
            return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE)
        }
    }
}