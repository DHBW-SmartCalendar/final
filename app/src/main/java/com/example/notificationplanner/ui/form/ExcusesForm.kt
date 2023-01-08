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
    // TODO Heading
// TODO first do todo 2 in NotificationConfig Class (the enum needs to implement DropDownCompatible interface)
// TODO implement Dropdown for excuse category (use States / on change : updating of data object is required) use NotificationType DropDown in NotificationCreationModal as Reference
// TODO implement slider for article amount (use States / on change : updating of data object is required) use WeatherForm as reference

    //var excuse_CategoryTemp = ""
    var category by remember { mutableStateOf(notificationConfig.excuses_category) }
    var amount by remember { mutableStateOf(notificationConfig.excuses_amount) }
    var sliderPosition by remember { mutableStateOf(1f) }

    Column(modifier = Modifier.padding(20.dp),){

        Text(text ="Choose excuse category", modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
        DropDownMenu(Excuse_Category.values().asList(), onSelectionChanged = {
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

        Text(text ="Choose amount of excuses", modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
        Slider(
            value = sliderPosition,
            modifier = Modifier.padding(15.dp),
            valueRange = 1f..10f,
            steps = 8,
            onValueChange = {sliderPosition = it; amount = it.toInt() },
            colors = SliderDefaults.colors(Color.Blue)
        )
        Text(text= sliderPosition.toInt().toString(), modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
    }



}