package com.example.notificationplanner.ui.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.notificationplanner.data.NotificationConfig

@Composable
fun CalendarForm (
    notificationConfig : NotificationConfig
){

    var sliderPosition by remember { mutableStateOf(notificationConfig.calendar_next_events_amount.toFloat()) }

    Column{
        Text(text = "How much ?", modifier = Modifier.align(alignment = Alignment.CenterHorizontally), color = Color.Black)
        Slider(
            value = sliderPosition,
            modifier = Modifier.padding(15.dp),
            valueRange = 1f..5f,
            steps = 3,
            onValueChange = { sliderPosition = it; notificationConfig.calendar_next_events_amount = it.toInt() },
            colors = SliderDefaults.colors(MaterialTheme.colorScheme.onSecondary)
        )
        Text(text = sliderPosition.toInt().toString(), modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
    }
}