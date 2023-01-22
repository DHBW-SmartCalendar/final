package com.example.notificationplanner.notifications.builder

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notificationplanner.R
import com.example.notificationplanner.exception.ExceptionNotification
import com.example.notificationplanner.jobs.SyncScheduledNotificationsJob
import com.example.notificationplanner.notifications.NotificationService
import com.example.notificationplanner.utils.CalendarProvider
import com.example.notificationplanner.utils.IntentProvider
import com.example.notificationplanner.utils.NotificationsConditions
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CalendarNotification : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context, intent: Intent?) {

        val uid = intent?.getIntExtra("uid", -1)
        Log.d(this@CalendarNotification.javaClass.name, "Received Intent with : Extra $uid")

        NotificationsConditions.check(context, uid!!) { _, config ->
            GlobalScope.launch(Dispatchers.IO) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
                    val eventList = CalendarProvider.readCalendarEvents(context, config.calendar_next_events_amount)

                    Log.d(this@CalendarNotification::class.java.name, "Calendar request was successful")

                    val notification = NotificationCompat.Builder(context, NotificationService.PLANNER_CHANNEL_ID)
                        .setSmallIcon(R.drawable.keiho_icon)
                        .setContentTitle("Next Calendar events")
                        .setStyle(NotificationCompat.BigTextStyle().bigText(getString(eventList)))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .build()

                    with(NotificationManagerCompat.from(context)) {
                        notify(5, notification)
                    }
                    Log.d(this@CalendarNotification::class.java.name, "Notification sent finally -> config uid : $uid")
                } else {
                    Log.e(this@CalendarNotification.javaClass.name, "Calendar permission not granted")
                    ExceptionNotification.sendExceptionNotification(context, "PLease check your permissions regarding reading calendar")
                }
            }
        }


        IntentProvider.pendingIntentBroadcast(
            context, 999999, SyncScheduledNotificationsJob::
            class.java
        ).send()
    }

    private fun getString(list: ArrayList<String>): String {
        val str = buildString {
            if (list.isNotEmpty()) {
                list.forEach {
                    append("$it \n")
                }
            } else {
                append("No upcoming events")
            }
        }
        return str
    }
}