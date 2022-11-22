package com.example.notificationplanner.ui.components


import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import com.example.notificationplanner.R
import com.example.notificationplanner.data.NotificationConfig
import com.example.notificationplanner.data.NotificationType
import com.example.notificationplanner.data.db.NotificationConfigRepository
import com.example.notificationplanner.jobs.AfterSomethingChangedJob
import com.example.notificationplanner.ui.form.*
import com.example.notificationplanner.ui.theme.Red40
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.*
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NotificationCreationModal(
    onClose: () -> Unit,
    notificationConfig: NotificationConfig?
) {

    Dialog(
        onDismissRequest = { onClose() },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        content = {
            val context = LocalContext.current

            val currentNotificationConfig by remember { mutableStateOf(notificationConfig ?: NotificationConfig()) }
            var currentNotificationType by remember { mutableStateOf(currentNotificationConfig.type ?: NotificationType.WEATHER) }
            var alarmClockSelected by remember { mutableStateOf(currentNotificationConfig.listenOnAlarm) }
            var calendarSelected by remember { mutableStateOf(currentNotificationConfig.listenOnCalendar) }
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
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { isGranted ->
                    hasNotificationPermission = isGranted
                }
            )


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, end = 10.dp),
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
                            // TODO temp
                            if (currentNotificationConfig.listenOnOwnTimer && !currentNotificationConfig.listenOnAlarm && !currentNotificationConfig.listenOnCalendar) {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                }
                                if (hasNotificationPermission) {
                                    currentNotificationConfig.isActive = true
                                }
                                save(currentNotificationConfig, context)
                                onClose()

                                val pendingIntent = PendingIntent.getBroadcast(
                                    context,
                                    1,
                                    Intent(context, AfterSomethingChangedJob::class.java),
                                    PendingIntent.FLAG_IMMUTABLE
                                )
                                pendingIntent.send()
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                }
                Text(
                    text = "Create Notification",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 10.dp, bottom = 10.dp)
                )
                Divider(thickness = 2.dp, modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 20.dp))

                Text(
                    text = "Notify if ...",
                    style = MaterialTheme.typography.titleLarge.plus(
                        TextStyle(
                            fontSize = 18.sp
                        )
                    ),
                    modifier = Modifier.padding(start = 25.dp, bottom = 5.dp)
                )
                Divider(thickness = 1.dp, modifier = Modifier.padding(start = 25.dp, end = 10.dp, bottom = 10.dp))


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
                        selected = alarmClockSelected

                    )
                    FilterChip(
                        onClick = {
                            calendarSelected = !calendarSelected

                        },
                        modifier = Modifier,
                        label = {
                            Text(text = "Calendar")
                        },
                        leadingIcon = {
                            Icon(painter = painterResource(id = R.drawable.calendar), contentDescription = "alarm icon")
                        },
                        selected = calendarSelected

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
                        selected = ownTimeSelected

                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                MaterialDialog(
                    dialogState = timePickerDialogState,
                    onCloseRequest = { timePickerDialogState.hide() },
                    buttons = {
                        positiveButton(text = "Ok", onClick = { currentNotificationConfig.timerTime = pickedTime.toString() })
                        negativeButton(text = "Cancel", onClick = { ownTimeSelected = false })
                    }
                ) {
                    timepicker(
                        initialTime = pickedTime,
                        title = "Pick a time",
                        is24HourClock = true
                    ) {
                        pickedTime = it
                    }
                }

                DropDownMenu(NotificationType.values().asList(), "Choose an option", onSelectionChanged = {
                    currentNotificationType = it
                })

                when (currentNotificationType) {
                    NotificationType.WEATHER -> WeatherForm(currentNotificationConfig)
                    NotificationType.NEWS -> NewsForm()
                    NotificationType.CALENDAR -> CalendarForm()
                    NotificationType.MEME -> MemeForm()
                    NotificationType.EXCUSE -> ExcusesForm()
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Bottom
                )
                {
                    Button(
                        modifier = Modifier,
                        onClick = {
                            delete(currentNotificationConfig, context)
                            onClose()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Red40)
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = Color.Black)
                    }
                }
            }
        }
    )
}

@OptIn(DelicateCoroutinesApi::class)
private fun save(notificationConfig: NotificationConfig, context: Context) {
    GlobalScope.launch(Dispatchers.IO) {
        val repo = NotificationConfigRepository(context = context)
        repo.addNotificationConfig(notificationConfig)

    }
}

@OptIn(DelicateCoroutinesApi::class)
private fun delete(notificationConfig: NotificationConfig, context: Context) {
    GlobalScope.launch(Dispatchers.IO) {
        val repo = NotificationConfigRepository(context = context)
        repo.deleteNotificationConfig(notificationConfig)

    }
}

private fun isValidated(config: NotificationConfig): Boolean {
    if (config.listenOnOwnTimer || config.listenOnCalendar || config.listenOnAlarm) {
        return !(config.listenOnOwnTimer && config.timerTime == null)
    }
    return false
}

fun onSaveResetAllOtherValues() {
    //TODO if object is going to be saved, all attributes from other Notification types must be false
}