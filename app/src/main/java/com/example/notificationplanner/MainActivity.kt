@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.notificationplanner

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.notificationplanner.ui.components.NotificationCard

import com.example.notificationplanner.ui.theme.NotificationPlannerTheme

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotificationPlannerTheme {
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
                                    // ModalDrawer
                                    startActivity(Intent(this@MainActivity, SecondActivity::class.java))
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
                            items(1) { NotificationCard() }
                        }
                    }
                }
            }
        }
    }
}
