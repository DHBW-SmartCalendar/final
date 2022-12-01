package com.example.notificationplanner.jobs

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.notificationplanner.data.NotificationConfig
import com.example.notificationplanner.data.db.NotificationConfigRepository
import com.example.notificationplanner.exception.ExceptionNotification
import com.example.notificationplanner.utils.DateNullException
import com.example.notificationplanner.utils.IntentProvider
import com.example.notificationplanner.z_old.ScheduledNotification
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.*
import java.time.format.DateTimeFormatter

class SyncScheduledNotificationsJob : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.S)
    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context, intent: Intent?) {

        GlobalScope.launch(Dispatchers.IO) {
            val repoConfig = NotificationConfigRepository(context = context)
            val day: Long = 86400000
            val configList = repoConfig.readAllData
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

            Log.d(this@SyncScheduledNotificationsJob.javaClass.name, " DB : ${configList.size} Elements in configList ")
            configList.forEach { config ->
                if (alarmManager != null && alarmManager.canScheduleExactAlarms()) {
                    if (config.isActive) {

                        if (config.listenOnOwnTimer) {
                            try {
                                val time = getUnixMillis(config.timerTime)
                                if (time - System.currentTimeMillis() > 0) {
                                    val notificationIntent = IntentProvider.pendingIntentBroadCast(context, config)
                                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, notificationIntent)
                                    Log.d(
                                        this@SyncScheduledNotificationsJob.javaClass.name,
                                        "Scheduled successful (own time) notification for :: ${config.timerTime} in Millis $time "
                                    )
                                } else {
                                    Log.w(this@SyncScheduledNotificationsJob.javaClass.name, "config : ${config.uid} is not in today's time range")
                                }
                            } catch (e: DateNullException) {
                                Log.e(this@SyncScheduledNotificationsJob.javaClass.name, "Failed to convert time")
                            }
                        }
                        if (config.listenOnAlarm) {
                            val notificationIntent = IntentProvider.pendingIntentBroadCast(context, config)
                            alarmManager.nextAlarmClock?.let {
                                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, it.triggerTime, notificationIntent)
                                Log.d(
                                    this@SyncScheduledNotificationsJob.javaClass.name,
                                    "Scheduled successful notification (listen for alarm clock) for :: ${millisToLocalDateTime(it.triggerTime)} in Millis ${it.triggerTime}"
                                )
                            } ?: run {
                                alarmManager.cancel(notificationIntent)
                                Log.d(this@SyncScheduledNotificationsJob.javaClass.name, "Canceled :: ${config.uid} , because alarm was turned off")

                            }
                        }
                    } else {
                        alarmManager.cancel(IntentProvider.pendingIntentBroadCast(context, config))
                        Log.d(this@SyncScheduledNotificationsJob.javaClass.name, "Canceled :: ${config.uid} ")
                    }
                } else {
                    Log.e(this@SyncScheduledNotificationsJob.javaClass.name, "Alarmmanager is not available ")
                    // TODO should be a pop up / snackbar when user opens the app next time
                    ExceptionNotification.sendExceptionNotification(context, "You need to turn on \"Can schedule exact alarms Permissions! \"")
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
