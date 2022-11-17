package com.example.notificationplanner.ui.form

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.notificationplanner.externAPI.ApiRequestClient

@Composable
fun WeatherForm() {
    val context = LocalContext.current
    Box {
        Text(text = "test")
        Button(onClick = {

            ApiRequestClient.request(context, "https://api.brightsky.dev/weather?lat=49.47&lon=8.54&date=2022-11-17")
        }) {
            Text(text = "request ")
        }
    }
}
