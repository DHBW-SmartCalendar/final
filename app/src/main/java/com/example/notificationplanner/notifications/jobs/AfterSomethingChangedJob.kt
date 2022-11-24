package com.example.notificationplanner.notifications.jobs

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.notificationplanner.data.NotificationConfig
import com.example.notificationplanner.data.db.NotificationConfigRepository
import com.example.notificationplanner.utils.DateNullException
import com.example.notificationplanner.utils.IntentProvider
import com.example.notificationplanner.z_old.OwnTimeScheduler
import com.example.notificationplanner.z_old.ScheduledNotification
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.*
import java.time.format.DateTimeFormatter

class AfterSomethingChangedJob : BroadcastReceiver() {

    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context, intent: Intent?) {

        GlobalScope.launch(Dispatchers.IO) {
            val repoConfig = NotificationConfigRepository(context = context)
            val day: Long = 86400000
            val configList = repoConfig.readAllData
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

            Log.d(this@AfterSomethingChangedJob.javaClass.name, " DB : ${configList.size} Elements in configList ")
            configList.forEach {
                if (alarmManager != null) {
                    if (it.isActive) {
                        if (it.listenOnOwnTimer) {
                            try {
                                val time = getUnixMillis(it.timerTime)
                                val notificationIntent = IntentProvider.pendingIntentBroadCast(context, it)
                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, day, notificationIntent)
                                Log.d(this@AfterSomethingChangedJob.javaClass.name, "Scheduled successful daily notification for :: ${it.timerTime}")
                            } catch (e: DateNullException) {
                                Log.e(this@AfterSomethingChangedJob.javaClass.name, "Failed to convert time")
                            }
                        }

                        if (it.listenOnAlarm) {
                            val time = alarmManager.nextAlarmClock.triggerTime
                            val notificationIntent = IntentProvider.pendingIntentBroadCast(context, it)
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, notificationIntent)
                            Log.d(
                                this@AfterSomethingChangedJob.javaClass.name,
                                "Scheduled successful notification (listen for alarm clock) for :: ${millisToLocalDateTime(time)}"
                            )
                        }

                    } else {
                        val notificationIntent = IntentProvider.pendingIntentBroadCast(context, it)
                        alarmManager.cancel(notificationIntent)
                    }
                } else {
                    Log.e(this@AfterSomethingChangedJob.javaClass.name, "Alarmmanager is not available ")
                }
            }
        }
    }

    private fun getUnixMillis(timeStr: String?): Long {
        return if (timeStr != null) {
            LocalDateTime.of(LocalDate.now(), LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm")))
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        } else {
            throw DateNullException("Time value is not available ")
        }
    }

    private fun List<ScheduledNotification>.containsWithUid(notificationConfig: NotificationConfig): Boolean {
        forEach {
            if (it.notificationConfigUid == notificationConfig.uid) {
                return true
            }
        }
        return false
    }

    private fun ScheduledNotification.exists(notificationConfigs: List<NotificationConfig>): Boolean {
        notificationConfigs.forEach {
            if (notificationConfigUid == it.uid) {
                return true
            }
        }
        return false
    }

    private fun millisToLocalDateTime(millis: Long): LocalDateTime {
        return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDateTime()
    }
}
