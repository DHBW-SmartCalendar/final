package com.example.notificationplanner.data

sealed class ConnectionState {
    object Available : ConnectionState()
    object Unavailable : ConnectionState()
}