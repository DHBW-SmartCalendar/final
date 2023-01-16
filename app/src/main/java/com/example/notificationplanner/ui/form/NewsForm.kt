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
import com.example.notificationplanner.externAPI.json.news.NewsCategory
import com.example.notificationplanner.ui.components.DropDownMenu

@Composable
fun NewsForm(notificationConfig: NotificationConfig) {

    var category by remember { mutableStateOf(notificationConfig.news_category) }
    var sliderPosition by remember { mutableStateOf(notificationConfig.news_amount.toFloat()) }

    Column {
        DropDownMenu(modifier = Modifier.padding(bottom = 10.dp), items = NewsCategory.values().asList(), onSelectionChanged = {
            category = it
            notificationConfig.news_category = category
        }, selected = notificationConfig.news_category)

        Text(text = "Article amount", modifier = Modifier.align(alignment = Alignment.CenterHorizontally), color = Color.Black)
        Slider(
            value = sliderPosition,
            modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 8.dp),
            valueRange = 1f..3f,
            steps = 1,
            onValueChange = { sliderPosition = it; notificationConfig.news_amount = it.toInt() },
            colors = SliderDefaults.colors(MaterialTheme.colorScheme.onSecondary)
        )
        Text(text = sliderPosition.toInt().toString(), modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
    }
}