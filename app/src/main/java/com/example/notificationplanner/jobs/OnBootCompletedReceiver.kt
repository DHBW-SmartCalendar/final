package com.example.notificationplanner.jobs

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.notificationplanner.utils.IntentProvider

class OnBootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action.equals(Intent.ACTION_BOOT_COMPLETED)){
            // TODO not optimal way -> request code
            IntentProvider.pendingIntentBroadCast(context, 999999, SyncScheduledNotificationsJob::class.java).send()
            Log.i(this.javaClass.name, "System booted -> synced scheduled notifications")
        }
    }
}