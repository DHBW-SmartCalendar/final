package com.example.notificationplanner.ui.components


import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.notificationplanner.R
import com.example.notificationplanner.data.NotificationConfig
import com.example.notificationplanner.data.NotificationType
import com.example.notificationplanner.data.db.NotificationRepository
import com.example.notificationplanner.ui.form.*
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


            val currentNotificationConfig by remember {
                mutableStateOf(
                    notificationConfig ?: NotificationConfig()
                )
            }
            var currentNotificationType by remember {
                mutableStateOf(
                    currentNotificationConfig.type ?: NotificationType.WEATHER
                )
            }
            var alarmClockSelected by remember {
                mutableStateOf(currentNotificationConfig.listenOnAlarm)
            }
            var calendarSelected by remember {
                mutableStateOf(currentNotificationConfig.listenOnCalendar)
            }
            var ownTimeSelected by remember {
                mutableStateOf(currentNotificationConfig.listenOnOwnTimer)
            }

            val timePickerDialogState = rememberMaterialDialogState(false)
            var pickedTime by remember {
                mutableStateOf(LocalTime.now())
            }
            val formattedTime by remember {
                derivedStateOf {
                    DateTimeFormatter
                        .ofPattern("HH:mm")
                        .format(pickedTime)
                }
            }


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
                        save(currentNotificationConfig, context)
                        onClose.invoke()
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
                        positiveButton(text = "Ok")
                        negativeButton(text = "Cancel", onClick = { ownTimeSelected = false })
                    }
                ) {
                    timepicker(
                        initialTime = LocalTime.now(),
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
                Button(onClick = {
                    println(currentNotificationConfig.weather_temperature)
                }) {
                    Text(text = "tets object")

                }


                //  content()
            }
        }
    )
}

@OptIn(DelicateCoroutinesApi::class)
private fun save(notificationConfig: NotificationConfig?, context: Context) {
    GlobalScope.launch(Dispatchers.IO) {
        val repo = NotificationRepository(context = context)
        repo.add(notificationConfig!!)

    }
}


fun onSaveResetAllOtherValues() {
    //TODO if object is going to be saved, all attributes from other Notification types must be false
}