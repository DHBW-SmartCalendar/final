@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.notificationplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.notificationplanner.Components.NotificationCard
import com.example.notificationplanner.ui.theme.DarkGray
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
                                onClick = { /*TODO*/ },
                                ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = Color.Black
                                )
                            }
                        }
                    ) { paddingValues ->
                        LazyColumn(
                            modifier =Modifier.fillMaxSize(),
                            contentPadding = paddingValues,
                            horizontalAlignment = Alignment.CenterHorizontally

                        ) {
                            item() { NotificationCard() }

                        }
                    }
                }
            }
        }
    }


}
