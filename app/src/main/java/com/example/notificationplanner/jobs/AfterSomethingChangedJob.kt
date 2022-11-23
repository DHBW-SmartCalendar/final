package com.example.notificationplanner.jobs

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.notificationplanner.data.NotificationConfig
import com.example.notificationplanner.z_old.ScheduledNotification
import com.example.notificationplanner.data.db.NotificationConfigRepository
import com.example.notificationplanner.z_old.ScheduledNotificationRepository
import com.example.notificationplanner.z_old.OwnTimeScheduler
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.stream.Collectors

class AfterSomethingChangedJob : BroadcastReceiver() {

    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context, intent: Intent?) {
        GlobalScope.launch(Dispatchers.IO) {
            val repoConfig = NotificationConfigRepository(context = context)
            val repoScheduled = ScheduledNotificationRepository(context = context)
            val configList = repoConfig.readAllData
            val scheduledList = repoScheduled.readAllData

            Log.d(this.javaClass.name, " DB : ${configList.size} Elements in configList ")
            Log.d(this.javaClass.name, " DB : ${scheduledList.size} Elements in scheduledList ")

            val work = configList.stream()
                .filter { it.isActive && !scheduledList.containsWithUid(it) }
                .map { it.uid }
                .collect(Collectors.toList()) as ArrayList<Int>

            val cancel = configList.stream()
                .filter { !it.isActive && scheduledList.containsWithUid(it) }
                .map { it.uid }
                .collect(Collectors.toList()) as ArrayList<Int>

            Log.d(this.javaClass.name, " : ${work.size} Elements in work")
            Log.d(this.javaClass.name, " : ${cancel.size} Elements in cancel")

            // if notificationConfig was deleted while being active -> scheduled notification should be deleted too
            scheduledList.forEach { if (!it.exists(configList)) cancel.add(it.notificationConfigUid) }





            val ownTimerSchedulerIntent = Intent(context, OwnTimeScheduler::class.java)
            ownTimerSchedulerIntent.putIntegerArrayListExtra("work", work)
            ownTimerSchedulerIntent.putIntegerArrayListExtra("cancel", cancel)
            val ownTimerSchedulerPendingIntent = PendingIntent.getBroadcast(
                context,
                2,
                ownTimerSchedulerIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
            ownTimerSchedulerPendingIntent.send()

            Log.i(this.javaClass.name, "send ownTimerSchedulerPendingIntent to Notification scheduler")
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
}
