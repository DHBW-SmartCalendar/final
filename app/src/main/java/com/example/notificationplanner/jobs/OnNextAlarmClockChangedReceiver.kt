package com.example.notificationplanner.jobs

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.notificationplanner.utils.IntentProvider

class OnNextAlarmClockChangedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action.equals(AlarmManager.ACTION_NEXT_ALARM_CLOCK_CHANGED)) {
            // TODO not optimal way -> request code
            IntentProvider.pendingIntentBroadCast(context, 999999, SyncScheduledNotificationsJob::class.java).send()
        }
    }
}