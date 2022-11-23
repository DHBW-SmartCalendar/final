package com.example.notificationplanner.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.provider.CalendarContract
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*


class CalendarProvider {

    private val permission: String = Manifest.permission.READ_CALENDAR
    private val requestCode: Int =1

    fun checkPermissionAndReadCalendar(activity: Activity){
        if(ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
        } else {
            readCalendarEvents(activity)
        }
    }

    fun readCalendarEvents(activity: Activity){
        val titleCol = CalendarContract.Events.TITLE
        val startDateCol = CalendarContract.Events.DTSTART
        val endDateCol = CalendarContract.Events.DTEND

        val projection = arrayOf(titleCol, startDateCol, endDateCol)
        val selection = CalendarContract.Events.DELETED + " != 1"

        val cursor = activity.contentResolver.query(    //resolves URI to the Calendar Provider
            CalendarContract.Events.CONTENT_URI, projection, selection, null, null
        )
        val titleColIdx = cursor!!.getColumnIndex(titleCol)
        val startDateColIdx = cursor.getColumnIndex(startDateCol)
        val endDateColIdx = cursor.getColumnIndex(endDateCol)

        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.GERMANY)

        while (cursor.moveToNext()){
            val title = cursor.getString(titleColIdx)
            val currentStartDate = formatter.format(Date(cursor.getLong(startDateColIdx)))
            val currentEndDate = formatter.format(Date(cursor.getLong(endDateColIdx)))

            Log.d("MY_APP", "$title $currentStartDate $currentEndDate")
        }

        cursor.close()
    }
}