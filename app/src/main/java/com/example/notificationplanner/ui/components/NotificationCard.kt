package com.example.notificationplanner.ui.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.notificationplanner.R
import com.example.notificationplanner.data.NotificationConfig
import com.example.notificationplanner.data.db.NotificationConfigRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@Composable
fun NotificationCard(
    modifier: Modifier = Modifier,
    notificationConfig: NotificationConfig,
    onEditRequest: () -> Unit
) {
    val context = LocalContext.current

    var hasNotificationPermission by remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            )
        } else mutableStateOf(true)
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasNotificationPermission = isGranted
        }
    )


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

        val configState by remember { mutableStateOf(notificationConfig) }
        var isChecked by remember { mutableStateOf(notificationConfig.isActive) }


        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = configState.type?.description!!,
                modifier = Modifier
                    .padding(20.dp)
                    .weight(4f),
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )
            Switch(
                checked = isChecked,
                onCheckedChange = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                    if (hasNotificationPermission) {
                        isChecked = it
                        configState.isActive = isChecked
                        saveActivation(configState, context)
                    }
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
        LazyRow(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom


        ) {
            item {
                IconButton(
                    onClick = { onEditRequest() }, modifier = Modifier
                        .border(1.dp, Color.Gray, RoundedCornerShape(20))
                        .size(35.dp, 35.dp)
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null, tint = Color.Gray)

                }

            }
            item {
                if (configState.listenOnOwnTimer && configState.timerTime != null) DigitalClock(
                    time = LocalTime.parse(configState.timerTime, DateTimeFormatter.ofPattern("HH:mm")),
                    height = 25,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                if (configState.listenOnAlarm) Icon(
                    painter = painterResource(id = R.drawable.alarm), contentDescription = null,
                    modifier = Modifier
                        .size(30.dp, 30.dp)
                        .padding(start = 10.dp),
                    tint = Color.Black
                )
                if (configState.listenOnCalendar) Icon(
                    painter = painterResource(id = R.drawable.calendar), contentDescription = null,
                    modifier = Modifier
                        .size(30.dp, 30.dp)
                        .padding(start = 10.dp),
                    tint = Color.Black
                )
            }
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
private fun saveActivation(notificationConfig: NotificationConfig, context: Context) {
    GlobalScope.launch(Dispatchers.IO) {
        val repo = NotificationConfigRepository(context = context)
        repo.addNotificationConfig(notificationConfig)

    }
}

