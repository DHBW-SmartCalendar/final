package com.example.notificationplanner.notifications.jobs

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.notificationplanner.data.db.NotificationConfigRepository
import com.example.notificationplanner.utils.IntentProvider
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class OnDeleteJob : BroadcastReceiver() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context, intent: Intent?) {
        GlobalScope.launch(Dispatchers.IO) {
            val uid = intent?.getIntExtra("uid", -1)
            val repoConfig = NotificationConfigRepository(context)
            if (uid != -1) {
                val config = repoConfig.findById(uid!!)
                if (config != null) {
                    val notificationIntent = IntentProvider.pendingIntentBroadCast(context, config)
                    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
                    alarmManager?.cancel(notificationIntent)
                    Log.d(this@OnDeleteJob.javaClass.name, "Canceled notification intent")
                    repoConfig.deleteNotificationConfig(config)
                    Log.d(this@OnDeleteJob.javaClass.name, "Deleted NotificationConfig uid:$uid")
                } else {
                    Log.e(this@OnDeleteJob.javaClass.name, "DB : data not found :: uid : $uid")
                }
            } else {
                Log.e(this@OnDeleteJob.javaClass.name, "Intent extra transmission went wrong")
            }
        }
    }
}