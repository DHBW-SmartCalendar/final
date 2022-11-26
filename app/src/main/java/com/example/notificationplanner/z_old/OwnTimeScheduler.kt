package com.example.notificationplanner.z_old

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.notificationplanner.data.NotificationConfig
import com.example.notificationplanner.data.NotificationType
import com.example.notificationplanner.data.db.NotificationConfigRepository
import com.example.notificationplanner.utils.DateNullException
import com.example.notificationplanner.utils.IntentProvider
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.*
import java.time.format.DateTimeFormatter

// TODO Permissions

class OwnTimeScheduler : BroadcastReceiver() {

    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(p0: Context, p1: Intent?) {
        val DAY: Long = 86400000
        val work = p1?.getIntegerArrayListExtra("work")
        val cancel = p1?.getIntegerArrayListExtra("cancel")

        GlobalScope.launch(Dispatchers.IO) {
            val repoConfig = NotificationConfigRepository(context = p0)

            work?.forEach {
                val config = repoConfig.readAllData.byId(it)
                val scheduledNotification = ScheduledNotification(
                    notificationType = config!!.type,
                    notificationTrigger = NotificationTrigger.OWN_TIME,
                    time = config.timerTime,
                    isDaily = true,
                    notificationConfigUid = config.uid
                )
                if (config.timerTime == null) {
                    Log.e(this.javaClass.name, "Time is null ")
                }







                when (config.type) {
                    NotificationType.WEATHER -> {
                        val weatherApiIntent = Intent(p0, WeatherAPIJob::class.java)
                        weatherApiIntent.putExtra("uid", config.uid)
                        weatherApiIntent.putExtra("time", getUnixMillis(scheduledNotification.time!!))
                        val pendingIntent = IntentProvider.pendingIntentBroadCast(p0, 3, weatherApiIntent)
                        val alarmManager = p0.getSystemService(Context.ALARM_SERVICE) as? AlarmManager



                        if ((getUnixMillis(scheduledNotification.time) - System.currentTimeMillis()) < 180000 /*3 min*/) {
                            alarmManager?.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000L, DAY, pendingIntent)
                        } else {
                            alarmManager?.setRepeating(
                                AlarmManager.RTC_WAKEUP,
                                getUnixMillis(scheduledNotification.time) - 150000,
                                DAY,
                                pendingIntent
                            )
                        }
                        Log.d(this@OwnTimeScheduler.javaClass.name, scheduledLogMessage(scheduledNotification))
                    }

                    //TODO
                    else -> {}
                }

            }
            cancel?.forEach {
                //TODO
            }
        }
    }

    private fun List<NotificationConfig>.byId(id: Int): NotificationConfig? {
        forEach { if (it.uid == id) return it }
        return null
    }

    private fun getUnixMillis(timeStr: String?): Long {
        return if (timeStr != null) {
            LocalDateTime.of(LocalDate.now(), LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm")))
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        } else {
            throw DateNullException()
        }
    }

    private fun scheduledLogMessage(scheduledNotification: ScheduledNotification): String {
        return "Scheduled Api call for ${getUnixMillis(scheduledNotification.time!!)}   " +
                "converted :  ${Instant.ofEpochMilli(getUnixMillis(scheduledNotification.time)).atZone(ZoneId.systemDefault()).toLocalDateTime()}"
    }

}