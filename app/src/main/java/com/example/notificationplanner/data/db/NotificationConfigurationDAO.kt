package com.example.notificationplanner.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.notificationplanner.data.NotificationConfig

@Dao
interface NotificationConfigurationDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNotificationConfiguration(nc: NotificationConfig)

    @Query("SELECT * FROM notification_configuration")
    fun readAllNC(): List<NotificationConfig>

}