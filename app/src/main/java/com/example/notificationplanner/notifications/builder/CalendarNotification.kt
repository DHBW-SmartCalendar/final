package com.example.notificationplanner.notifications.builder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.notificationplanner.jobs.SyncScheduledNotificationsJob
import com.example.notificationplanner.utils.IntentProvider

class CalendarNotification : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        // use WeatherNotification as reference for implementing this notification
        // notification id = 5


        IntentProvider.pendingIntentBroadcast(context, 999999, SyncScheduledNotificationsJob::class.java).send()
    }
}