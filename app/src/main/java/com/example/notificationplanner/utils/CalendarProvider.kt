package com.example.notificationplanner.utils

import android.content.Context
import android.provider.CalendarContract
import java.text.SimpleDateFormat
import java.util.*


class CalendarProvider {

    companion object {
        fun readCalendarEvents(context: Context, amount: Int): ArrayList<String> {
            val titleCol = CalendarContract.Events.TITLE
            val startDateCol = CalendarContract.Events.DTSTART
            val endDateCol = CalendarContract.Events.DTEND

            val projection = arrayOf(titleCol, startDateCol, endDateCol)
            val selection = CalendarContract.Events.DELETED + " != 1"

            val cursor = context.contentResolver.query(    //resolves URI to the Calendar Provider
                CalendarContract.Events.CONTENT_URI, projection, selection, null, null
            )
            val titleColIdx = cursor!!.getColumnIndex(titleCol)
            val startDateColIdx = cursor.getColumnIndex(startDateCol)
            val endDateColIdx = cursor.getColumnIndex(endDateCol)

            val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.GERMANY)
            var counter = 0
            val list = ArrayList<String>()
            while (cursor.moveToNext() && counter < amount) {
                val title = cursor.getString(titleColIdx)
                val currentStartDate = Date(cursor.getLong(startDateColIdx))
                val currentEndDate = Date(cursor.getLong(endDateColIdx))

                if (currentStartDate.after(Date(System.currentTimeMillis()))) {
                    list.add("$title ${formatter.format(currentStartDate)} - ${formatter.format(currentEndDate)}")
                    counter++
                }
            }
            cursor.close()
            return list
        }
    }
}