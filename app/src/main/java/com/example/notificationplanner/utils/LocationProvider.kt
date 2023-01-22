package com.example.notificationplanner.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat

class LocationProvider {
    companion object {


        fun getLocation(context: Context): Location? {
            val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                return lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }
            Log.w(this::class.java.name, "Location Permissions not granted")
            return null

        }
    }
}