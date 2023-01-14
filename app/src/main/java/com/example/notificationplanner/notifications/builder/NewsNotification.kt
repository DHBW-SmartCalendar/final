package com.example.notificationplanner.notifications.builder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.Html
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notificationplanner.R
import com.example.notificationplanner.data.NotificationConfig
import com.example.notificationplanner.exception.ExceptionNotification
import com.example.notificationplanner.externAPI.json.news.News
import com.example.notificationplanner.notifications.NotificationService
import com.example.notificationplanner.utils.NotificationsConditions
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NewsNotification : BroadcastReceiver() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context, intent: Intent?) {

        val uid = intent?.getIntExtra("uid", -1)
        Log.d(this@NewsNotification.javaClass.name, "Received Intent with : Extra $uid")

        NotificationsConditions.check(context, uid!!) { api, config ->
            GlobalScope.launch(Dispatchers.IO) {
                val response = api.getNews(config.news_category.toString().toLowerCase())
                println("AFTER RESPONSE, RESPONSE is: " + response)
                if (response.isSuccessful) {
                    Log.d(this@NewsNotification::class.java.name, "News Api request was successful")

                    val notification = NotificationCompat.Builder(context, NotificationService.PLANNER_CHANNEL_ID)
                        .setSmallIcon(R.drawable.img_da)
                        .setContentTitle("News")
                        .setStyle(NotificationCompat.BigTextStyle().bigText(getNewsString(config, response.body()!!)))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .build()

                    with(NotificationManagerCompat.from(context)) {
                        notify(1, notification)
                    }
                    Log.d(this@NewsNotification::class.java.name, "Notification sent finally -> config uid : $uid")
                } else {
                    Log.e(this@NewsNotification.javaClass.name, "Api call failure ")
                    ExceptionNotification.default(context)
                }
            }
        }
    }


    private fun getNewsString(config: NotificationConfig, news: News): String{
        val str = buildString {
            for (index in 0 until config.excuses_amount){
                append("" + news.articles[index].title + "\n \n")
            }
        }
        return str
    }
}