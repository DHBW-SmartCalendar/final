package com.example.notificationplanner.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.notificationplanner.R
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NotificationCreationModal(
    onClose: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = { onClose() },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        content = {

            var alarmClockSelected by remember {
                mutableStateOf(false)
            }
            var calendarSelected by remember {
                mutableStateOf(false)
            }
            var ownTimeSelected by remember {
                mutableStateOf(false)
            }
            var timePickerDialogState = rememberMaterialDialogState(false)
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
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { onClose() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                }
                Text(
                    text = "Create Notification",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 10.dp, bottom = 10.dp)
                )
                Divider(thickness = 1.dp, modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp))

                // Listener Selection
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {


                    FilterChip(
                        onClick = { alarmClockSelected = !alarmClockSelected },
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
                        onClick = { calendarSelected = !calendarSelected },
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
                        negativeButton(text = "Cancel")
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








                content()
            }
        }
    )
}