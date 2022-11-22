package com.example.notificationplanner.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.notificationplanner.data.NotificationConfig

@Dao
interface NotificationConfigDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNotificationConfiguration(nc: NotificationConfig)

    @Query("SELECT * FROM notification_configuration")
    fun readAllNotificationConfigurations(): List<NotificationConfig>

    @Delete
    suspend fun deleteNotificationConfiguration(notificationConfig: NotificationConfig)

    @Query("SELECT * FROM notification_configuration WHERE uid=:id ")
    fun findNotificationConfigById(id: Int): NotificationConfig?

}