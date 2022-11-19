package com.example.notificationplanner.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun ConfigureContent(modifier: Modifier = Modifier,){
    Column(
    ) {
        Text(text ="Configure Content",
            modifier = Modifier.padding(20.dp),
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black,
        )
        Divider(thickness = 1.dp, modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 3.dp))
        DropdownDemo()
    }
}

//@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun NotificationDropDown(
    modifier: Modifier = Modifier,
){

    DropdownDemo()
}



@Composable
@Preview
fun DropdownDemo() {
    var expanded by remember { mutableStateOf(false) }
    val items = listOf("A", "B", "C", "D", "E", "F")
    val disabledValue = "B"
    var selectedIndex by remember { mutableStateOf(0) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
    ) {
        Text(
            items[selectedIndex], modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { expanded = true })
                .background(
                    Color.Gray
                )
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.Gray
                )
        ) {
            items.forEachIndexed { index, s ->
                DropdownMenuItem(
                    text = { "Test" },
                    onClick = {
                        selectedIndex = index
                        expanded = false
                        val disabledText = if (s == disabledValue) {
                            " (Disabled)"
                        } else {
                            ""
                        }
                    },
                ) /*{
                    val disabledText = if (s == disabledValue) {
                        " (Disabled)"
                    } else {
                        ""
                    }
                    Text(text = s + disabledText)
                }*/
            }
        }
    }

}

/*
fun DropdownMenuItem(onClick: () -> Unit, interactionSource: () -> Unit) {

}*/
