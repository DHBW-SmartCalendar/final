package com.example.notificationplanner

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import com.example.notificationplanner.notifications.NotificationService.Companion.PLANNER_CHANNEL_ID
import com.example.notificationplanner.jobs.OnNextAlarmClockChangedService
import com.example.notificationplanner.jobs.SyncScheduledNotificationsJob
import com.example.notificationplanner.utils.IntentProvider
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

class App : Application() {

    val DAY: Long = 86400000
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        applicationContext.startForegroundService(Intent(applicationContext, OnNextAlarmClockChangedService::class.java))
        registerDailySync()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            PLANNER_CHANNEL_ID,
            "Planner",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    private fun registerDailySync() {
        val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            LocalDateTime.of(LocalDate.now().plusDays(1L), LocalTime.of(0, 1)).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            DAY,
            IntentProvider.pendingIntentBroadCast(applicationContext, 999999, SyncScheduledNotificationsJob::class.java)
        )
    }
}
