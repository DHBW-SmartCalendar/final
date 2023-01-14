package com.example.notificationplanner.ui.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import com.example.notificationplanner.externAPI.json.news.NewsCategory
import com.example.notificationplanner.ui.components.DropDownMenu

@Composable
fun NewsForm(notificationConfig: NotificationConfig) {
    Text(text = "NewsForm")
    // TODO Heading
// TODO first do todo 1 in NotificationConfig Class (the enum needs to implement DropDownCompatible interface)
// TODO implement Dropdown for news topic (use States / on change : updating of data object is required) use NotificationType DropDown in NotificationCreationModal as Reference
// TODO implement slider for article amount ->(use States / on change : updating of data object is required) use WeatherForm as reference


    var category by remember { mutableStateOf(notificationConfig.news_category) }
    var sliderPosition by remember { mutableStateOf(1f) }

    Column(modifier = Modifier.padding(20.dp),){

        Text(text ="Choose news category", modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
        DropDownMenu(items = NewsCategory.values().asList(), onSelectionChanged = {
            category = it
        })

        when (category) {
            NewsCategory.BUSINESS -> category = NewsCategory.BUSINESS
            NewsCategory.ENTERTAINMENT -> category = NewsCategory.ENTERTAINMENT
            NewsCategory.GENERAL -> category = NewsCategory.GENERAL
            NewsCategory.HEALTH -> category = NewsCategory.HEALTH
            NewsCategory.SCIENCE -> category = NewsCategory.SCIENCE
            NewsCategory.SPORTS -> category = NewsCategory.SPORTS
            NewsCategory.TECHNOLOGY -> category = NewsCategory.TECHNOLOGY
            NewsCategory.SPORTS -> category = NewsCategory.SPORTS
            NewsCategory.SPORTS -> category = NewsCategory.SPORTS

        }
        notificationConfig.news_category = category

        Text(text ="Choose amount of excuses", modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
        Slider(
            value = sliderPosition,
            modifier = Modifier.padding(15.dp),
            valueRange = 1f..3f,
            steps = 1,
            onValueChange = {sliderPosition = it; notificationConfig.news_amount = it.toInt() },
            colors = SliderDefaults.colors(Color.Blue)
        )
        Text(text= sliderPosition.toInt().toString(), modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
    }
}