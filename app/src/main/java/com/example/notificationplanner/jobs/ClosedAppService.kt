package com.example.notificationplanner.jobs

import android.app.AlarmManager
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder

class ClosedAppService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        registerReceiver(OnNextAlarmClockChangedReceiver(), IntentFilter(AlarmManager.ACTION_NEXT_ALARM_CLOCK_CHANGED))
   //     registerReceiver(OnBootCompletedReceiver(), IntentFilter(Intent.ACTION_BOOT_COMPLETED))
    }
}