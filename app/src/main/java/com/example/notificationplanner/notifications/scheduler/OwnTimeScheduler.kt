package com.example.notificationplanner.notifications.scheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.notificationplanner.data.NotificationConfig
import com.example.notificationplanner.data.ScheduledNotification
import com.example.notificationplanner.data.db.NotificationConfigRepository
import com.example.notificationplanner.data.db.NotificationTrigger
import com.example.notificationplanner.data.db.ScheduledNotificationRepository
import com.example.notificationplanner.notifications.WeatherNotification
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
        val work = p1?.getIntegerArrayListExtra("work")
        val cancel = p1?.getIntegerArrayListExtra("cancel")

        GlobalScope.launch(Dispatchers.IO) {
            val repoConfig = NotificationConfigRepository(context = p0)
            val repoScheduled = ScheduledNotificationRepository(context = p0)

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

       /*         val pendingIntent = PendingIntent.getBroadcast(
                    p0,
                    3,
                    Intent(p0, WeatherNotification::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )


*/
                val pendingIntent = IntentProvider.pendingIntentBroadCast(p0, 3, WeatherNotification::class.java)

                val alarmManager = p0.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
                alarmManager?.setExact(AlarmManager.RTC_WAKEUP, getUnixMillis(scheduledNotification.time!!), pendingIntent)

                Log.d(
                    this@OwnTimeScheduler.javaClass.name,
                    "Scheduled Notification for ${getUnixMillis(scheduledNotification.time!!)}   " +
                            "converted :  ${
                                Instant.ofEpochMilli(getUnixMillis(scheduledNotification.time)).atZone(ZoneId.systemDefault()).toLocalDateTime()
                            }"
                )

                repoScheduled.addScheduledNotification(scheduledNotification)

            }
            cancel?.forEach {  }
        }
    }

    private fun List<NotificationConfig>.byId(id: Int): NotificationConfig? {
        forEach { if (it.uid == id) return it }
        return null
    }

    private fun getUnixMillis(timeStr: String): Long {
        return LocalDateTime.of(LocalDate.now(), LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm")))
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

}