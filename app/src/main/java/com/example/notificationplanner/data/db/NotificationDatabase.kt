package com.example.notificationplanner.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notificationplanner.data.NotificationConfig

@Database(
    entities = [
        NotificationConfig::class
    ],
    version = 3,
    exportSchema = false
)
abstract class NotificationDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationConfigurationDAO

    companion object {
        @Volatile
        private var INSTANCE: NotificationDatabase? = null

        //ensure that there is only one instance of database
        fun getDatabase(context: Context): NotificationDatabase {
            if (INSTANCE != null) {
                return INSTANCE as NotificationDatabase
            }
            //ensure nobody can access instance while creating
            synchronized(this) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    NotificationDatabase::class.java,
                    "notification_database"

                ).fallbackToDestructiveMigration().build()
                return INSTANCE as NotificationDatabase
            }
        }
    }
}