package com.example.notificationplanner.ui.form

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.notificationplanner.externAPI.APIClient
import com.example.notificationplanner.externAPI.BaseURL
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun WeatherForm() {
    Box {
        Text(text = "test")
        Button(onClick = {
            APIClient.request(BaseURL.WEATHER)
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
    }
}
