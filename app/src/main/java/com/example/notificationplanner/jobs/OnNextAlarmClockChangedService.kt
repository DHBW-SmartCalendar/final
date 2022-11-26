package com.example.notificationplanner.jobs

import android.app.AlarmManager
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder

class OnNextAlarmClockChangedService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        registerReceiver(OnNextAlarmClockChangedReveiver(), IntentFilter(AlarmManager.ACTION_NEXT_ALARM_CLOCK_CHANGED))
    }
}