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
import com.example.notificationplanner.ui.components.DropDownMenu

@Composable
fun ExcusesForm (notificationConfig: NotificationConfig){

    var category by remember { mutableStateOf(notificationConfig.excuses_category) }
    var sliderPosition by remember { mutableStateOf(1f) }

    Column(modifier = Modifier.padding(20.dp),){

        Text(text ="Choose excuse category", modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
        DropDownMenu(items = Excuse_Category.values().asList(), onSelectionChanged = {
            category = it
        })

        when (category) {
            Excuse_Category.FAMILY -> category = Excuse_Category.FAMILY
            Excuse_Category.OFFICE -> category = Excuse_Category.OFFICE
            Excuse_Category.CHILDREN -> category = Excuse_Category.CHILDREN
            Excuse_Category.COLLEGE -> category = Excuse_Category.COLLEGE
            Excuse_Category.PARTY -> category = Excuse_Category.PARTY
            Excuse_Category.FUNNY -> category = Excuse_Category.FUNNY
            Excuse_Category.UNBELIEVABLE -> category = Excuse_Category.UNBELIEVABLE
            Excuse_Category.DEVELOPERS -> category = Excuse_Category.DEVELOPERS
            Excuse_Category.GAMING -> category = Excuse_Category.GAMING
        }
        notificationConfig.excuses_category = category

        Text(text ="Choose amount of excuses", modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
        Slider(
            value = sliderPosition,
            modifier = Modifier.padding(15.dp),
            valueRange = 1f..5f,
            steps = 3,
            onValueChange = {sliderPosition = it; notificationConfig.excuses_amount = it.toInt() },
            colors = SliderDefaults.colors(Color.Blue)
        )
        Text(text= sliderPosition.toInt().toString(), modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
    }



}