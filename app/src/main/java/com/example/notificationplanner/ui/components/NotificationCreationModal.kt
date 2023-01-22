package com.example.notificationplanner.ui.components


import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.notificationplanner.R
import com.example.notificationplanner.data.NotificationConfig
import com.example.notificationplanner.data.NotificationType
import com.example.notificationplanner.data.db.NotificationConfigRepository
import com.example.notificationplanner.jobs.SyncScheduledNotificationsJob
import com.example.notificationplanner.ui.form.*
import com.example.notificationplanner.utils.IntentProvider
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.*
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NotificationCreationModal(
    onClose: () -> Unit,
    notificationConfig: NotificationConfig?,
    isEditing: Boolean
) {

    Dialog(
        onDismissRequest = { onClose() },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        content = {
            val context = LocalContext.current

            val currentNotificationConfig by remember { mutableStateOf(notificationConfig ?: NotificationConfig()) }
            var currentNotificationType by remember { mutableStateOf(currentNotificationConfig.type) }
            var alarmClockSelected by remember { mutableStateOf(currentNotificationConfig.listenOnAlarm) }
            val calendarSelected by remember { mutableStateOf(currentNotificationConfig.listenOnCalendar) }
            var ownTimeSelected by remember { mutableStateOf(currentNotificationConfig.listenOnOwnTimer) }
            val timePickerDialogState = rememberMaterialDialogState(false)

            var pickedTime by remember {
                mutableStateOf(
                    if (currentNotificationConfig.timerTime != null) LocalTime.parse(
                        currentNotificationConfig.timerTime,
                        DateTimeFormatter.ofPattern("HH:mm")
                    ) else LocalTime.of(0, 0)
                )
            }
            val formattedTime by remember {
                derivedStateOf {
                    DateTimeFormatter
                        .ofPattern("HH:mm")
                        .format(pickedTime)
                }
            }

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
            val backgroundLocationLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { isGranted ->
                    hasBackgroundLocationPermission = isGranted
                }
            )
            val locationPermissionsLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions(),
                onResult = { isGranted ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        hasNotificationPermission = isGranted[Manifest.permission.POST_NOTIFICATIONS] ?: false
                    }
                    hasCoarseLocationPermission = isGranted[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
                    hasFineLocationPermission = isGranted[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
                    backgroundLocationLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                }
            )


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(top = 15.dp, end = 15.dp, bottom = 25.dp, start = 15.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { onClose() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                    IconButton(onClick = {
                        currentNotificationConfig.listenOnAlarm = alarmClockSelected
                        currentNotificationConfig.listenOnCalendar = calendarSelected
                        currentNotificationConfig.listenOnOwnTimer = ownTimeSelected
                        currentNotificationConfig.type = currentNotificationType
                        if (isValidated(currentNotificationConfig)) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                if (currentNotificationConfig.type == NotificationType.WEATHER) {
                                    locationPermissionsLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.POST_NOTIFICATIONS,
                                            Manifest.permission.ACCESS_COARSE_LOCATION,
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                        )
                                    )
                                } else {
                                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                }
                            }
                            if (hasNotificationPermission) {
                                currentNotificationConfig.isActive = true
                            }

                            save(currentNotificationConfig, context)
                            println("YOUR NOTIFICATION:  $currentNotificationConfig")
                            IntentProvider.pendingIntentBroadcast(context, 999999, SyncScheduledNotificationsJob::class.java).send()
                            onClose()

                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10),
                    shadowElevation = 6.dp,
                    color = MaterialTheme.colorScheme.secondary
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 20.dp, end = 20.dp, bottom = 10.dp, start = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {


                        Text(
                            text = "Create Notification",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )

                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp)
                        )


                        Text(
                            text = "Notify when:",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .padding(bottom = 5.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Start

                        )


                        // Listener Selection
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            FilterChip(
                                onClick = {
                                    alarmClockSelected = !alarmClockSelected
                                },
                                modifier = Modifier,
                                label = {
                                    Text(text = "Alarm Clock")
                                },
                                leadingIcon = {
                                    Icon(painter = painterResource(id = R.drawable.alarm), contentDescription = "alarm icon")
                                },
                                selected = alarmClockSelected,
                                border = FilterChipDefaults.filterChipBorder(
                                    selectedBorderColor = Color.Black,
                                    selectedBorderWidth = 1.dp,
                                    borderColor = MaterialTheme.colorScheme.onSecondary,
                                    borderWidth = 2.dp
                                ),
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = MaterialTheme.colorScheme.secondary,
                                    selectedContainerColor = MaterialTheme.colorScheme.onSecondary
                                )


                            )
                            FilterChip(
                                onClick = {
                                    if (!ownTimeSelected) {
                                        timePickerDialogState.show()
                                    }
                                    ownTimeSelected = !ownTimeSelected
                                },
                                modifier = Modifier,
                                label = {
                                    if (ownTimeSelected) {
                                        Text(text = formattedTime)
                                    } else {
                                        Text(text = "Daily")
                                    }
                                },
                                leadingIcon = {
                                    Icon(painter = painterResource(id = R.drawable.stopwatch), contentDescription = "alarm icon")
                                },
                                selected = ownTimeSelected,
                                border = FilterChipDefaults.filterChipBorder(
                                    selectedBorderColor = Color.Black,
                                    selectedBorderWidth = 1.dp,
                                    borderColor = MaterialTheme.colorScheme.onSecondary,
                                    borderWidth = 2.dp
                                ),
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = MaterialTheme.colorScheme.secondary,
                                    selectedContainerColor = MaterialTheme.colorScheme.onSecondary
                                )
                            )
                        }


                        MaterialDialog(
                            dialogState = timePickerDialogState,
                            onCloseRequest = { timePickerDialogState.hide() },
                            buttons = {
                                positiveButton(
                                    text = "Ok",
                                    onClick = { currentNotificationConfig.timerTime = pickedTime.toString() },
                                    textStyle = TextStyle(color = Color.Black)
                                )
                                negativeButton(text = "Cancel", onClick = { ownTimeSelected = false }, textStyle = TextStyle(color = Color.Black))
                            }
                        ) {
                            timepicker(
                                initialTime = pickedTime,
                                title = " ",
                                is24HourClock = true,
                                colors = TimePickerDefaults.colors(
                                    activeBackgroundColor = MaterialTheme.colorScheme.secondary,
                                    selectorColor = MaterialTheme.colorScheme.onSecondary,
                                )
                            ) {
                                pickedTime = it
                            }
                        }
                        Divider(
                            thickness = 1.dp,
                            modifier = Modifier
                                .padding(top = 10.dp, bottom = 20.dp, start = 10.dp, end = 10.dp),
                            color = Color.Black
                        )
                        Text(
                            text = "Topic:",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .padding(bottom = 15.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )

                        DropDownMenu(items = NotificationType.values().asList(), onSelectionChanged = {
                            currentNotificationType = it
                            println(it)
                        }, selected = currentNotificationType)
                        Divider(
                            thickness = 1.dp,
                            modifier = Modifier
                                .padding(top = 20.dp, bottom = 20.dp, start = 10.dp, end = 10.dp),
                            color = Color.Black
                        )
                        Text(
                            text = "Options:",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .padding(bottom = 5.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )


                        when (currentNotificationType) {
                            NotificationType.WEATHER -> WeatherForm(currentNotificationConfig)
                            NotificationType.NEWS -> NewsForm(currentNotificationConfig)
                            NotificationType.CALENDAR -> CalendarForm()
                            NotificationType.MEME -> MemeForm(currentNotificationConfig)
                            NotificationType.EXCUSE -> ExcusesForm(currentNotificationConfig)
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Bottom
                        )
                        {
                            if (isEditing) IconButton(
                                onClick = {
                                    delete(notificationConfig!!, context)
                                    onClose()
                                },
                                colors = IconButtonDefaults.iconButtonColors()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSecondary,
                                    modifier = Modifier.size(45.dp, 45.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@OptIn(DelicateCoroutinesApi::class)
private fun save(notificationConfig: NotificationConfig, context: Context) {
    GlobalScope.launch(Dispatchers.IO) {
        val repo = NotificationConfigRepository(context)
        repo.addNotificationConfig(notificationConfig)

    }
}

@OptIn(DelicateCoroutinesApi::class)
private fun delete(notificationConfig: NotificationConfig, context: Context) {
    val uid = notificationConfig.uid
    GlobalScope.launch(Dispatchers.IO) {
        val repoConfig = NotificationConfigRepository(context)
        try {
            val config = repoConfig.findById(uid)
            if (config != null) {
                val notificationIntent = IntentProvider.pendingIntentBroadcast(context, config)
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
                alarmManager?.cancel(notificationIntent)
                Log.d("NotificationCreationModal", "Canceled notification intent $uid")
                repoConfig.deleteNotificationConfig(config)
                Log.d("NotificationCreationModal", "Deleted NotificationConfig uid:$uid")
            } else {
                Log.e("NotificationCreationModal", "DB : data not found :: uid : $uid")
            }
        } catch (e: Exception) {
            Log.e("NotificationCreationModal", "DB Error $uid")
        }
    }
}

private fun isValidated(config: NotificationConfig): Boolean {
    if (config.listenOnOwnTimer || config.listenOnCalendar || config.listenOnAlarm) {
        return !(config.listenOnOwnTimer && config.timerTime == null)
    } else {
        Log.w("Modal", "nothing chosen")
    }
    return false
}