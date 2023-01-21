package com.example.notificationplanner.notifications.builder

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.notificationplanner.R
import com.example.notificationplanner.exception.ExceptionNotification
import com.example.notificationplanner.externAPI.json.meme.Meme
import com.example.notificationplanner.notifications.NotificationService
import com.example.notificationplanner.utils.NotificationsConditions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MemeNotification : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent?) {

        val uid = intent?.getIntExtra("uid", -1)
        Log.d(this@MemeNotification.javaClass.name, "Received Intent with : Extra $uid")

        NotificationsConditions.check(context, uid!!) { api, config ->
            GlobalScope.launch(Dispatchers.IO) {
                val response = api.getMeme(1)
                if (response.isSuccessful) {
                    Log.d(this@MemeNotification::class.java.name, "News Api request was successful")

                    val notification = NotificationCompat.Builder(context, NotificationService.PLANNER_CHANNEL_ID)
                        .setSmallIcon(R.drawable.img_da)
                        .setContentTitle("Meme")
                        .setStyle(NotificationCompat.BigPictureStyle().bigPicture(getMemePicture(response.body()!!, context)))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .build()

                    with(NotificationManagerCompat.from(context)) {
                        notify(1, notification)
                    }
                    Log.d(this@MemeNotification::class.java.name, "Notification sent finally -> config uid : $uid")
                } else {
                    Log.e(this@MemeNotification.javaClass.name, "Api call failure ")
                    ExceptionNotification.sendExceptionNotification(context)
                }
            }
        }
    }

    private suspend fun getMemePicture(meme: Meme, context: Context): Bitmap {
        val loading = ImageLoader(context)
        val request = ImageRequest.Builder(context).data("${meme.memes[0].url}").build()
        val result = (loading.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }
}