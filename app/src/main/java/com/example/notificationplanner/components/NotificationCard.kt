package com.example.notificationplanner.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.notificationplanner.ui.theme.Green30
import com.example.notificationplanner.ui.theme.Grey20
import com.example.notificationplanner.ui.theme.Grey90

@Preview
@Composable
fun NotificationCard(
    title: String = "Title",
) {

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(100.dp),

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
                style = MaterialTheme.typography.titleMedium
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
                    checkedThumbColor = Green30,
                    checkedTrackColor = Grey90,
                    uncheckedThumbColor = Grey20,
                    uncheckedTrackColor = Grey90,
                    checkedBorderColor = Grey90,
                    uncheckedBorderColor = Grey90

                ),

            )

        }
    }
}
