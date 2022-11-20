package com.example.notificationplanner.jobs

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.notificationplanner.notifications.BroadcastTest
import java.util.Calendar

class SchedulerTest {
    companion object {

        fun schedule(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
            val next = alarmManager?.nextAlarmClock
            println("Alarm _________________" + next?.triggerTime)

            val time = Calendar.getInstance()
            time.timeInMillis = System.currentTimeMillis()
            time.add(Calendar.SECOND, 30)

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                2,
                Intent(context, BroadcastTest::class.java),
                PendingIntent.FLAG_IMMUTABLE
            )
            //alarmManager?.setExact(AlarmManager.RTC_WAKEUP, time.timeInMillis, pendingIntent)
            alarmManager?.setRepeating(AlarmManager.RTC_WAKEUP, time.timeInMillis, 10000, pendingIntent)

        }
    }
}