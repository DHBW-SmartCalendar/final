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
import com.example.notificationplanner.externAPI.json.excuses.Excuse_Category
import com.example.notificationplanner.ui.components.DropDownMenu

@Composable
fun ExcusesForm (){
    // TODO Heading
// TODO first do todo 2 in NotificationConfig Class (the enum needs to implement DropDownCompatible interface)
// TODO implement Dropdown for excuse category (use States / on change : updating of data object is required) use NotificationType DropDown in NotificationCreationModal as Reference
// TODO implement slider for article amount (use States / on change : updating of data object is required) use WeatherForm as reference

    var excuse_CategoryTemp = ""
    var excuseCategory: Excuse_Category by remember { mutableStateOf(Excuse_Category.FAMILY) }
    var sliderPosition by remember { mutableStateOf(0f) }

    Column(modifier = Modifier.padding(20.dp),){

        Text(text ="Choose excuse category", modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
        DropDownMenu(Excuse_Category.values().asList(), onSelectionChanged = {
            excuseCategory = it
        })

        when (excuseCategory) {
            Excuse_Category.FAMILY -> excuse_CategoryTemp = "family"
            Excuse_Category.OFFICE -> excuse_CategoryTemp = "office"
            Excuse_Category.CHILDREN -> excuse_CategoryTemp = "children"
            Excuse_Category.COLLEGE -> excuse_CategoryTemp = "college"
            Excuse_Category.PARTY -> excuse_CategoryTemp = "party"
            Excuse_Category.FUNNY -> excuse_CategoryTemp = "funny"
            Excuse_Category.UNBELIEVABLE -> excuse_CategoryTemp = "unbelievable"
            Excuse_Category.DEVELOPERS -> excuse_CategoryTemp = "developers"
            Excuse_Category.GAMING -> excuse_CategoryTemp = "gaming"
        }

        Text(text ="Choose amount of excuses", modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
        Slider(
            value = sliderPosition,
            modifier = Modifier.padding(15.dp),
            valueRange = 0f..10f,
            steps = 9,
            onValueChange = {sliderPosition = it},
            colors = SliderDefaults.colors(Color.Blue)
        )
        Text(text= sliderPosition.toInt().toString(), modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
    }



}