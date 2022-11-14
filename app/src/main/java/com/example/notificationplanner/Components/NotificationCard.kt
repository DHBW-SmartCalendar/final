package com.example.notificationplanner.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp

@Composable
fun NotificationCard() {


    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(20))
            .fillMaxWidth(0.9f)
            .height(100.dp)
            .shadow(20.dp)

        ,
        colors = CardDefaults.elevatedCardColors(
            containerColor =MaterialTheme.colorScheme.primary,


        )



    ){

    }

    
}
