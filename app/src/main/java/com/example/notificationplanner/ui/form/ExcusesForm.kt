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
import com.example.notificationplanner.externAPI.json.excuses.Excuse_Category
import com.example.notificationplanner.ui.components.DropDownMenu

@Composable
fun ExcusesForm(notificationConfig: NotificationConfig) {

    var category by remember { mutableStateOf(notificationConfig.excuses_category) }
    var sliderPosition by remember { mutableStateOf(notificationConfig.excuses_amount.toFloat()) }

    Column {
        DropDownMenu(modifier = Modifier.padding(bottom = 10.dp),items = Excuse_Category.values().asList(), onSelectionChanged = {
            category = it
            notificationConfig.excuses_category = category
        }, selected = category)

        Text(text = "How much ?", modifier = Modifier.align(alignment = Alignment.CenterHorizontally), color = Color.Black)
        Slider(
            value = sliderPosition,
            modifier = Modifier.padding(15.dp),
            valueRange = 1f..5f,
            steps = 3,
            onValueChange = { sliderPosition = it; notificationConfig.excuses_amount = it.toInt() },
            colors = SliderDefaults.colors(MaterialTheme.colorScheme.onSecondary)
        )
        Text(text = sliderPosition.toInt().toString(), modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
    }


}