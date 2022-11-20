package com.example.notificationplanner.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BroadcastTest  : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        println("Received ++++++++++++++++++++++++++++++")
        println("Received ++++++++++++++++++++++++++++++")
        println("Received ++++++++++++++++++++++++++++++")

    }
}