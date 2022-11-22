package com.example.notificationplanner.data.db

import androidx.room.*
import com.example.notificationplanner.data.ScheduledNotification

@Dao
interface ScheduledNotificationDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addScheduledNotification(sn : ScheduledNotification)

    @Query("SELECT * FROM scheduled_notification")
    fun readAllScheduledNotifications(): List<ScheduledNotification>

    @Delete
    suspend fun deleteScheduledNotification(ScheduledNotification: ScheduledNotification)

    @Query("SELECT * FROM scheduled_notification WHERE uid=:id ")
    fun findScheduledNotificationById(id: Int): ScheduledNotification?

}