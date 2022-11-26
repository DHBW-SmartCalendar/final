@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.notificationplanner

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.notificationplanner.data.NotificationConfig
import com.example.notificationplanner.data.db.NotificationConfigRepository
import com.example.notificationplanner.jobs.SyncScheduledNotificationsJob
import com.example.notificationplanner.utils.IntentProvider
import com.example.notificationplanner.ui.components.NotificationCard
import com.example.notificationplanner.ui.components.NotificationCreationModal
import com.example.notificationplanner.ui.theme.NotificationPlannerTheme
import com.example.notificationplanner.utils.CalendarProvider
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


// TODO if list is empty show some text

        setContent {
            val context = LocalContext.current
            val activity = this

            NotificationPlannerTheme {

                val notificationList = remember { mutableStateListOf<NotificationConfig>() }
                var createDialogIsOpen by remember { mutableStateOf(false) }
                var editing: NotificationConfig? by remember { mutableStateOf(null) }
                val editDialogIsOpen by remember { derivedStateOf { editing != null } }
                var isSynced by remember { mutableStateOf(false) }


                LaunchedEffect(key1 = createDialogIsOpen, key2 = editing) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val repo = NotificationConfigRepository(context = context)
                        val list = repo.readAllData
                        withContext(Dispatchers.Main) {
                            notificationList.swapList(list)
                            isSynced = true
                        }
                    }
                }

                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        floatingActionButton = {
                            FloatingActionButton(
                                containerColor = MaterialTheme.colorScheme.tertiary,
                                onClick = {
                                    createDialogIsOpen = true
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        },
                        topBar = {
                            CenterAlignedTopAppBar(
                                title = {
                                    Text(text = "Planner")
                                },
                                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.tertiary,
                                    titleContentColor = Color.White
                                ),
                                modifier = Modifier.height(40.dp)
                            )
                        }
                    ) { paddingValues ->
                        if (isSynced) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 10.dp),
                                contentPadding = paddingValues,
                                horizontalAlignment = Alignment.CenterHorizontally

                            ) {
                                items(notificationList) {
                                    NotificationCard(notificationConfig = it, modifier = Modifier.padding(bottom = 15.dp), onEditRequest = {
                                        editing = it
                                    })
                                }

                                // testing
                                item {
                                    Button(onClick = {
                                        val notificationIntent = Intent(context, SyncScheduledNotificationsJob::class.java)
                                       IntentProvider.pendingIntentBroadCast(context, 99, notificationIntent).send()
                                    }) {
                                        Text(text = "test test test")
                                    }
                                }
                                item {
                                    //Just for Testing Calendar ReadOut, delete this button later
                                    Button(
                                        onClick = {
                                            val c = CalendarProvider()
                                            c.checkPermissionAndReadCalendar(activity)
                                        },
                                    ) {
                                        Text(text = "Press to Log Calendar Events")
                                    }
                                }
                            }




                            if (createDialogIsOpen) NotificationCreationModal(
                                onClose = {
                                    createDialogIsOpen = false
                                    isSynced = false
                                },
                                notificationConfig = null
                            )

                            if (editDialogIsOpen) NotificationCreationModal(
                                onClose = {
                                    editing = null
                                    isSynced = false
                                },
                                notificationConfig = editing
                            )
                        } else {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(
                                    color = Color.Black,
                                    strokeWidth = 4.dp
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun <T> SnapshotStateList<T>.swapList(newList: List<T>) {
        clear()
        addAll(newList)
    }


    override fun onStop() {
        super.onStop()
        println("destroy ++++++++++++++++++++++++++++++++")
    }

}
