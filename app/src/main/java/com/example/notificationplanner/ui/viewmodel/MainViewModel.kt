package com.example.notificationplanner.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notificationplanner.data.NotificationConfig
import com.example.notificationplanner.data.db.NotificationConfigRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(context: Context) : ViewModel() {

    private lateinit var repository: NotificationConfigRepository

    private var _notList = mutableStateListOf<NotificationConfig>()

    val notificationConfigList: List<NotificationConfig>
        get() = _notList

    private var _isSynced = mutableStateOf(false)

    val isSynced: Boolean
        get() = _isSynced.value

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository = NotificationConfigRepository(context = context)
        }
    }


    private fun loadNotificationsConfigs() {
        viewModelScope.launch(Dispatchers.IO) {
            _notList.swapList(repository.readAllData)
        }
    }

    private fun insertNotificationConfiguration(notificationConfig: NotificationConfig) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNotificationConfig(notificationConfig)
            loadNotificationsConfigs()
        }
    }

    private fun deleteNotificationConfiguration(notificationConfig: NotificationConfig) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNotificationConfig(notificationConfig)
            loadNotificationsConfigs()
        }
    }

    fun synced() {
        _isSynced.value = true
    }

    fun notSynced() {
        _isSynced.value = false
    }


    private fun <T> SnapshotStateList<T>.swapList(newList: List<T>) {
        clear()
        addAll(newList)
    }

}




