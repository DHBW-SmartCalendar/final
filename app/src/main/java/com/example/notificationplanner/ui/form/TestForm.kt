package com.example.notificationplanner.ui.form

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.notificationplanner.data.NotificationConfig
import com.example.notificationplanner.externAPI.APIClient
import com.example.notificationplanner.data.NotificationType
import com.example.notificationplanner.data.db.NotificationConfigRepository
import kotlinx.coroutines.*

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun TestForm() {

    val context = LocalContext.current

    val testNConfig = NotificationConfig(
        isActive = false,
        listenOnAlarm = false,
        listenOnCalendar = false,
        listenOnOwnTimer = false,
        type = NotificationType.WEATHER
    )

    Text(text = "Weather Content Configuration")
    Button(onClick = {
        APIClient.request(NotificationType.WEATHER)
        {
            GlobalScope.launch(Dispatchers.IO) {
                val w = it.getWeather()
                if (w.isSuccessful) {
                    println(w.body().toString())
                }
            }
        }
    })
    {
        Text(text = "request ")
    }
    Button(onClick = {
        GlobalScope.launch(Dispatchers.IO) {
            val repo = NotificationConfigRepository(context)
            repo.addNotificationConfig(testNConfig)
            println("added to dp +++++++++++++++++++++++++")
        }
    })
    {
        Text(text = "DB  ")
    }

    Button(onClick = {
        GlobalScope.launch(Dispatchers.IO) {
            val repo = NotificationConfigRepository(context)
            val data = repo.readAllData
            println("${data.size} +++++++++++++++++++++++++")
        }
    })
    {
        Text(text = "request DB  ")
    }

}
