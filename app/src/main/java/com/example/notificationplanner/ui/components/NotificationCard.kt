package com.example.notificationplanner.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.notificationplanner.R

@Preview
@Composable
fun NotificationCard(
    modifier: Modifier = Modifier,
    title: String = "Title"
) {

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .then(modifier),

        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 20.dp,
            hoveredElevation = 25.dp
        )
    ) {
        var checkedValue by remember { mutableStateOf(false) }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = title,
                modifier = Modifier
                    .padding(20.dp)
                    .weight(4f),
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )
            Switch(
                checked = checkedValue,
                onCheckedChange = {
                    checkedValue = it
                    println(it)
                },
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1f),
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFFFFFEFE),
                    checkedTrackColor = Color(0xFF2DD158),
                    checkedBorderColor = Color(0xFF2DD158),
                    uncheckedThumbColor = Color(0xFFFFFEFE),
                    uncheckedTrackColor = if (isSystemInDarkTheme()) Color(0xFF393A3D) else Color(0xFFE8E9EB),
                    uncheckedBorderColor = if (isSystemInDarkTheme()) Color(0xFF393A3D) else Color(0xFFE8E9EB)
                )
            )


        }
        Divider(thickness = 1.dp, modifier = Modifier.padding(start = 10.dp, end = 10.dp))
        LazyRow (
                ){
            item {
                Image(
                    painter = painterResource(id = R.drawable.wecker_png), contentDescription = "alarmClock",
                    modifier = Modifier
                        .size(55.dp, 55.dp)
                        .padding(10.dp)
                )
            }
            item {
                Image(
                    painter = painterResource(id = R.drawable.google_calendar_icon), contentDescription = "alarmClock",
                    modifier = Modifier
                        .size(55.dp, 55.dp)
                        .padding(10.dp)
                )
            }
        }


    }
}

