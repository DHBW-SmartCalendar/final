@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.notificationplanner

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.notificationplanner.data.NotificationConfig
import com.example.notificationplanner.data.db.NotificationRepository
import com.example.notificationplanner.ui.components.NotificationCard
import com.example.notificationplanner.ui.components.NotificationCreationModal
import com.example.notificationplanner.ui.theme.NotificationPlannerTheme
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


// TODO after editing ui does not update automatically

        setContent {
            val context = LocalContext.current

            NotificationPlannerTheme {

                var notificationList: List<NotificationConfig> by remember { mutableStateOf(emptyList()) }
                var createDialogIsOpen by remember { mutableStateOf(false) }
                var editing: NotificationConfig? by remember { mutableStateOf(null) }
                val editDialogIsOpen by remember { derivedStateOf { editing != null } }

                var tester by remember { mutableStateOf(1) }

                // TODO busy-indicator
                LaunchedEffect(key1 = createDialogIsOpen, key2 = editing) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val repo = NotificationRepository(context = context)
                        val list = repo.readAllData
                        withContext(Dispatchers.Main) {
                            notificationList = list
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

                                    //startActivity(Intent(this@MainActivity, SecondActivity::class.java))
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

                            item {
                                Button(onClick = { println("list +++++++++++++++++++++++++ ${notificationList.size}") }) {
                                    Text(text = tester.toString())

                                }
                            }
                        }

                        if (createDialogIsOpen) NotificationCreationModal(onClose = { createDialogIsOpen = false }, notificationConfig = null)
                        if (editDialogIsOpen) NotificationCreationModal(onClose = {
                            editing = null
                            tester++
                        }, notificationConfig = editing)


                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        println("destroy ++++++++++++++++++++++++++++++++")
    }

}
