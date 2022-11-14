package com.example.notificationplanner.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp

@Composable
fun NotificationCard() {

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

    }


}
