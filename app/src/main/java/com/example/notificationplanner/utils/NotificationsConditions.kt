package com.example.notificationplanner.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.notificationplanner.data.NotificationConfig
import com.example.notificationplanner.data.NotificationType
import com.example.notificationplanner.data.db.NotificationConfigRepository
import com.example.notificationplanner.exception.ExceptionNotification
import com.example.notificationplanner.externAPI.APIClient
import com.example.notificationplanner.externAPI.APICollection
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

class NotificationsConditions {
    companion object {
        @OptIn(DelicateCoroutinesApi::class)
        fun check(context: Context, uid: Int, sendNotification: (APICollection?, NotificationConfig) -> Unit) {
            GlobalScope.launch(Dispatchers.IO) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                    // DB request & API call
                    if (uid != -1) {
                        val repoConfig = NotificationConfigRepository(context)
                        val duration = measureTimeMillis {
                            val config = repoConfig.findById(uid)
                            if (config != null) {
                                if (InternetConnection.check(context)) {
                                    if (config.type == NotificationType.CALENDAR) {
                                        sendNotification(null, config)
                                    } else {
                                        APIClient.request(config.type) {
                                            sendNotification(it, config)
                                        }
                                    }
                                } else {
                                    Log.e("NotificationsConditions", "Internet connection not available")
                                    ExceptionNotification.sendExceptionNotification(
                                        context,
                                        "To receive weather notifications your device needs to be connected to the internet"
                                    )
                                }
                            } else {
                                Log.e("NotificationsConditions", "Config not found in db")
                                ExceptionNotification.sendExceptionNotification(context)
                            }
                        }
                        Log.d("NotificationsConditions", "Duration for DB and API request + sending Notification  :: $duration ms ")

                    } else {
                        Log.e("NotificationsConditions", "Intent Extras check failed ::: value -> $uid")
                        ExceptionNotification.sendExceptionNotification(context)
                    }
                } else {
                    Log.d("NotificationsConditions", "Permissions not granted")
                    //TODO show dialog / snackbar
                }
            }
        }
    }
}