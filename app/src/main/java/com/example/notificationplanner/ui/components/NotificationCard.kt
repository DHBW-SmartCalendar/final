package com.example.notificationplanner.ui.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.notificationplanner.R
import com.example.notificationplanner.data.NotificationConfig
import com.example.notificationplanner.data.NotificationType
import com.example.notificationplanner.data.db.NotificationConfigRepository
import com.example.notificationplanner.jobs.SyncScheduledNotificationsJob
import com.example.notificationplanner.utils.IntentProvider
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


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
    var hasCoarseLocationPermission by remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            )
        } else mutableStateOf(true)
    }
    var hasFineLocationPermission by remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            )
        } else mutableStateOf(true)
    }
    var hasBackgroundLocationPermission by remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
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
    val fineLocationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasFineLocationPermission = isGranted
        }
    )
    val coarseLocationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasCoarseLocationPermission = isGranted
            fineLocationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

        }
    )
    val backgroundLocationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasBackgroundLocationPermission = isGranted
        }
    )

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .then(modifier),
        shape = RoundedCornerShape(20),

        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 20.dp,
            hoveredElevation = 25.dp,
            pressedElevation = 30.dp
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
                text = configState.type.description + " "
                        + when (configState.type) {
                    NotificationType.NEWS -> configState.news_category.getLabelText()
                    else -> {
                        " "
                    }
                },
                modifier = Modifier
                    .padding(15.dp)
                    .weight(4f),
                style = MaterialTheme.typography.displayMedium,
            )
            Switch(
                checked = isChecked,
                onCheckedChange = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        if (notificationConfig.type == NotificationType.WEATHER) {
                            coarseLocationLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                            backgroundLocationLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                        }
                    }
                    if (hasNotificationPermission) {
                        isChecked = it
                        configState.isActive = isChecked
                        saveActivation(configState, context)
                        IntentProvider.pendingIntentBroadcast(context, 999999, SyncScheduledNotificationsJob::class.java).send()
                    }
                },
                modifier = Modifier
                    .padding(top = 6.dp, end = 6.dp)
                    .weight(1f),
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFFFFFEFE),
                    uncheckedThumbColor = Color(0xFFFFFEFE),
                    checkedTrackColor = MaterialTheme.colorScheme.onSecondary,
                    uncheckedTrackColor = Color(0xFF9E948D),
                    checkedBorderColor = MaterialTheme.colorScheme.onSecondary,
                    uncheckedBorderColor = Color(0xFF9E948D)
                )
            )


        }
        Divider(thickness = 2.dp, modifier = Modifier.padding(start = 10.dp, end = 100.dp), color = Color.White)
        LazyRow(
            modifier = Modifier
                .padding(top = 10.dp, bottom = 12.dp, start = 12.dp, end = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom


        ) {
            item {
                IconButton(
                    onClick = { onEditRequest() }, modifier = Modifier
                        .border(1.dp, Color.White, RoundedCornerShape(20))
                        .size(35.dp, 35.dp)
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null, tint = Color.White)

                }

            }
            item {
                if (configState.listenOnOwnTimer && configState.timerTime != null) DigitalClock(
                    time = configState.timerTime!!,
                    height = 25,
                    //modifier = Modifier.padding(bottom = 2.dp)
                )
                if (configState.listenOnAlarm) Icon(
                    painter = painterResource(id = R.drawable.alarm), contentDescription = null,
                    modifier = Modifier
                        .size(32.dp, 32.dp)
                        .padding(start = 10.dp, top = 4.dp),
                    tint = Color.White
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

