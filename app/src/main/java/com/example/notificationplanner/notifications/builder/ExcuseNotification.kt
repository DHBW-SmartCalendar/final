package com.example.notificationplanner.notifications.builder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notificationplanner.R
import com.example.notificationplanner.data.NotificationConfig
import com.example.notificationplanner.exception.ExceptionNotification
import com.example.notificationplanner.externAPI.json.excuses.Excuse
import com.example.notificationplanner.externAPI.json.excuses.ExcuseItem
import com.example.notificationplanner.notifications.NotificationService
import com.example.notificationplanner.utils.NotificationsConditions
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.*


class ExcuseNotification : BroadcastReceiver() {

    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context, intent: Intent?) {
        println("WORKS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        // TODO implement api function to get excuses -> ApiCollection
        // use WeatherNotification as reference for implementing this notification

        val uid = intent?.getIntExtra("uid", -1)
        Log.d(this@ExcuseNotification.javaClass.name, "Received Intent with : Extra $uid")

        NotificationsConditions.check(context, uid!!) { api, config ->
            GlobalScope.launch(Dispatchers.IO) {
                val response = api.getExcuse(config.excuses_category.toString().toLowerCase(), config.excuses_amount)
                println("RESPONSE: " + response)
                Log.d(this@ExcuseNotification::class.java.name,"HERE RESPONSE !!!!!!!!!!!!!!: $response")
                if (response.isSuccessful) {
                    Log.d(this@ExcuseNotification::class.java.name, "Excuse Api request was successful")
                    val excuse = getExcuse(response.body()!!)

                    val notification = NotificationCompat.Builder(context, NotificationService.PLANNER_CHANNEL_ID)
                        .setSmallIcon(R.drawable.img_da)
                        .setContentTitle("Excuse")
                        .setStyle(NotificationCompat.BigTextStyle().bigText(getExcuseString(config, excuse!!)))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .build()

                    with(NotificationManagerCompat.from(context)) {
                        notify(1, notification)
                    }
                    Log.d(this@ExcuseNotification::class.java.name, "Notification sent finally -> config uid : $uid")
                } else {
                    Log.e(this@ExcuseNotification.javaClass.name, "Api call failure ")
                    ExceptionNotification.default(context)
                }
            }
        }
    }

    private fun getExcuse(excuseItem: ExcuseItem): Excuse{
        val excuse1: Excuse = Excuse()
        excuse1.add(excuseItem)
        return excuse1
    }

    private fun getExcuseString(config: NotificationConfig, excuse: Excuse): String{
        val str = buildString {
            append("Category        : \n")
            append("Excuse          : \n")
        }
        return str
    }
}