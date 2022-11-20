package com.example.notificationplanner.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Formatter

@Composable
fun DigitalClock(
    time: LocalTime,
    height: Int,
    modifier: Modifier = Modifier

) {
    Box(
        modifier = Modifier
            .size(height = height.dp, width = (2 * height).dp)
            .border(1.dp, Color.Black, RoundedCornerShape(25)).then(modifier),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = DateTimeFormatter
                .ofPattern("HH:mm")
                .format(time),
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp,
            color = Color.Black

        )
    }

}